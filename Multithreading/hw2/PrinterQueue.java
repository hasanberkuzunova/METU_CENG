import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class PrinterQueue implements IMPMCQueue<PrintItem>
{
    // TODO: This is all yours
    private PriorityQueue<PrintItem> queue;

    private ReentrantLock lock;

    private Condition queueFull;

    private Condition queueClosed;

    private int max;
    private static final AtomicBoolean closeRoom=new AtomicBoolean(false);

    public PrinterQueue(int maxElementCount)
    {
        max=maxElementCount;
        queue = new PriorityQueue<>(maxElementCount, new PrintItemComparator());
        lock = new ReentrantLock();
        queueFull = lock.newCondition();
        queueClosed = lock.newCondition();
    }

    @Override
    public void Add(PrintItem data) throws QueueIsClosedExecption {
        lock.lock();
        if (closeRoom.get()) {
            queueClosed.signalAll();
            lock.unlock();
            throw new QueueIsClosedExecption();
        }
        try {
            while(queue.size()==max && !closeRoom.get()){
                queueFull.await();
            }
            if (closeRoom.get()) {
                queueClosed.signalAll();
                throw new QueueIsClosedExecption();
            }
            queue.add(data);
            queueClosed.signalAll();
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
        finally {
            lock.unlock();
        }
    }


    @Override
    public PrintItem Consume() throws QueueIsClosedExecption {
        lock.lock();
        if (queue.isEmpty() && closeRoom.get()) {
            queueFull.signalAll();
            lock.unlock();
            throw new QueueIsClosedExecption();
        }
        try {
            while (queue.isEmpty()) {
            queueClosed.await();
        }
            if (queue.isEmpty() && closeRoom.get()) {
                queueFull.signalAll();
                throw new QueueIsClosedExecption();
            }
            queueFull.signalAll();
            return queue.poll();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
         finally {
            lock.unlock();
        }
        return queue.poll();
    }


    @Override
    public int RemainingSize() {
            return this.max-this.queue.size();
    }


    @Override
    public void CloseQueue() {
        lock.lock();
        try {
            closeRoom.set(true);
            queueFull.signalAll();
            queueClosed.signalAll();
        }
        finally {
        lock.unlock();
        }
    }
}

