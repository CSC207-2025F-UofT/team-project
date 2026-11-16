package view.canvas;

// success / error toasts, page doesn't care who draws them
interface Notices {
    void success(String text);

    void error(String title, String reason);

    void clearError();  // problem got fixed

    Runnable progress(String text);  // run what comes back when the work is done
}
