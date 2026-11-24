package use_case.admin_login;

import entity.Admin;

/**
 * What the admin login use case needs from storage.
 */
public interface AdminLoginDataAccessInterface {
    Admin getAdmin();
}
