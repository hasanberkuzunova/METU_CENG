import java.util.Comparator;

public class PrintItemComparator implements Comparator<PrintItem> {
    @Override
    public int compare(PrintItem item1, PrintItem item2) {

        if (item1.getPrintType() == PrintItem.PrintType.INSTRUCTOR && item2.getPrintType() == PrintItem.PrintType.STUDENT) {
            return -1;
        }

        if (item1.getPrintType() == PrintItem.PrintType.STUDENT && item2.getPrintType() == PrintItem.PrintType.INSTRUCTOR) {
            return 1;
        }

        return 0;
    }
}
