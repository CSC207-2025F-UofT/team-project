package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile {

    public enum UserRole {
    BUYER,
    SELLER
}

    public static class WishlistEntry {
        private final Item item;
        private final String listingLink;

        public WishlistEntry(Item item, String listingLink) {
            this.item = item;
            this.listingLink = listingLink;
        }

        public Item getItem() {
            return item;
        }

        public String getListingLink() {
            return listingLink;
        }
    }

    private final UserRole role;
    private final List<WishlistEntry> wishlist;

    public UserProfile(UserRole role, List<WishlistEntry> wishlist) {
        this.role = role;
        this.wishlist = new ArrayList<>(wishlist);
    }

    public UserRole getRole() {
        return role;
    }

    public List<WishlistEntry> getWishlist() {
        return Collections.unmodifiableList(new ArrayList<>(wishlist));
    }
}

