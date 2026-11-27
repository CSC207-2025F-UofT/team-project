package interface_adapter.import_photos;

import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

public class ImportPhotosViewModel extends ViewModel<ImportPhotosState> {
    public ImportPhotosViewModel() {
        super(ViewNames.IMPORT_PHOTOS);
        setState(new ImportPhotosState());
    }
}
