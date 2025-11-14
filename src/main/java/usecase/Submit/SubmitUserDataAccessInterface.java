package usecase.Submit;

import java.io.File;
import java.io.IOException;

public interface SubmitUserDataAccessInterface {
    void submit(File StudentFile) throws IOException;
}
