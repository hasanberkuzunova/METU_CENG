import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Thread;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterRoom
{
    private IMPMCQueue<PrintItem> roomQueue;
    private final List<Printer> printers;


    private class Printer implements Runnable
    {
        private int id;
        private IMPMCQueue<PrintItem> queue;
        private Thread thr;
        public Printer(int id, IMPMCQueue<PrintItem> roomQueue)
        {
            this.id = id;
            this.queue = roomQueue;
            this.thr=new Thread(this);
            thr.start();
            SyncLogger.Instance().Log(SyncLogger.ThreadType.MAIN_THREAD, 0, String.format(SyncLogger.FORMAT_PRINTER_LAUNCH,id));
        }

        @Override
        public void run() {
            while (true) {

                try {
                    PrintItem item = queue.Consume();
                    item.print();
                    SyncLogger.Instance().Log(SyncLogger.ThreadType.CONSUMER, id,String.format(SyncLogger.FORMAT_PRINT_DONE, item));
                } catch (QueueIsClosedExecption e) {
                    break;
                }

            }
        }

    }

    public PrinterRoom(int printerCount, int maxElementCount)
    {

        // Instantiating the shared queue
        roomQueue = new PrinterQueue(maxElementCount);

        // Let's try streams
        // Printer creation automatically launches its thread
        printers = Collections.unmodifiableList(IntStream.range(0, printerCount)
                                                         .mapToObj(i -> new Printer(i, roomQueue))
                                                         .collect(Collectors.toList()));
        // Printers are launched using the same queue
    }

    public boolean SubmitPrint(PrintItem item, int producerId)
    {
        SyncLogger.Instance().Log(SyncLogger.ThreadType.PRODUCER, producerId,String.format(SyncLogger.FORMAT_ADD, item));
        try {
            roomQueue.Add(item);
            return true;
        } catch (QueueIsClosedExecption e) {
            return false;
        }
    }


    public void CloseRoom()
    {
        roomQueue.CloseQueue();
        for (Printer printer : printers) {
            while (printer.thr.isAlive()) {
                try {
                   printer.thr.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
