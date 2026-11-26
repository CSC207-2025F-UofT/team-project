package interface_adapter.Favorite;

/** A view implements this interface to react to favourite state changes. */
public interface FavoriteViewModelListener {
    void onFavoriteStateChanged(FavoriteState state);
}
