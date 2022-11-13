import java.util.ArrayList;

public class Navigation
{
    private ArrayList<Page> pages = new ArrayList<>();
    private Page currentPage;

    public void open(int pageIndex)
    {
        currentPage.closePage();
        pages.get(pageIndex).openPage();
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }
}
