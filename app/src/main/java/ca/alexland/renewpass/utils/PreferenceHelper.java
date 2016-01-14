package ca.alexland.renewpass.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AlexLand on 2015-12-30.
 */
public class PreferenceHelper {
    private static final String RENEWPASS_PREFERENCES = "RenewPass Preferences";
    private static final String FIRST_RUN_PREFERENCE = "First Run";
    private static final String USERNAME_PREFERENCE = "Username";
    private static final String PASSWORD_PREFERENCE = "Password";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private KeyStoreUtil keyStoreUtil;
    private boolean keysExist;

    public PreferenceHelper(Context context) {
        this.settings = context.getSharedPreferences(RENEWPASS_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = settings.edit();
    }

    public boolean getFirstRun() {
        return settings.getBoolean(FIRST_RUN_PREFERENCE, true);
    }

    public void setFirstRun(boolean firstRun) {
        editor.putBoolean(FIRST_RUN_PREFERENCE, firstRun);
        editor.commit();
    }

    public String getUsername() {
        return settings.getString(USERNAME_PREFERENCE, "");
    }

    public String getPassword() {
        // TODO: Decrypt password
        return settings.getString(PASSWORD_PREFERENCE, "");
    }

    public void addUsername(String username) {
        editor.putString(USERNAME_PREFERENCE, username);
        editor.commit();
    }

    public void addPassword(String password) {
        // TODO: Encrypt password
        editor.putString(PASSWORD_PREFERENCE, password);
        editor.commit();
    }

    public boolean credentialsEntered() {
        return !getUsername().equals("") && !getPassword().equals("");
    }

    public void setupKeys(Context context) {
        this.keyStoreUtil = new KeyStoreUtil(this.getUsername());
        keysExist = keyStoreUtil.createKeys(context);
    }
}