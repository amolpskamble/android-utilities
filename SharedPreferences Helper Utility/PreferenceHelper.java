import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

/**
 * Singleton instance of utility class to save key/value pair data in {@link SharedPreferences}
 */
public class PreferenceHelper {
    /**
     * Singleton instance for the @{@link PreferenceHelper}
     * Initialise once and use from anywhere during app lifecycle
     */
    public static PreferenceHelper instance;
    /**
     * {@link SharedPreferences} object to store data
     */
    private SharedPreferences sharedPreferences;
    /**
     * {@link SharedPreferences.Editor} object to manipulate @{@link SharedPreferences}
     */
    private SharedPreferences.Editor editor;
    /**
     * A unique tag for class logging purpose.
     */
    public static String TAG = PreferenceHelper.class.getSimpleName();
    /**
     * {@link Gson} from google for json parsing. Need for storing and fetch java model classes
     */
    Gson gson;

    /**
     * Get {@link SharedPreferences}
     * @return sharedPreferences
     */
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * Constructor for Preference Helper.
     * Private access given to prevent user from creating object
     * Context.MODE_PRIVATE is used.
     * Initialised {@link SharedPreferences.Editor} and {@link SharedPreferences}
     * Initialised @{@link Gson}
     * @param context Android Application Context
     */
    private PreferenceHelper(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    /**
     * Remove all keys and respective data from the {@link SharedPreferences}
     */
    public void removeAll() {
        editor.clear().commit();
    }

    /**
     * Initialise the utility {@link PreferenceHelper}
     * Create Singleton object.
     * @param context Android Application Context
     * @return instance
     */
    public static PreferenceHelper init(Context context) {
        if (instance == null) {
            instance = new PreferenceHelper(context);
        }
        return instance;
    }

    /**
     * Get Singleton instance of {@link PreferenceHelper}
     * @return instance
     */
    public static PreferenceHelper getInstance() {
        return instance;
    }

    /**
     * Remove the key from @{@link SharedPreferences}
     * @param key key to be removed
     */
    public void remove(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit();
        }
    }

    /**
     * Save the given value in {@link SharedPreferences}
     * @param key key associate the data
     * @param value value to be stored
     */
    public void savePref(String key, Object value) {
        delete(key);

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Non-primitive preferences are not allowed");
        }

        editor.commit();
    }

    /**
     *
     * @param key key to be fetched
     * @param <T> Generic return type
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);

    }


    @SuppressWarnings("unchecked")
    public <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    /**
     * Check if given key is exist in {@link SharedPreferences}
     * @param key key to be checked
     * @return boolean
     */
    public boolean isPrefExists(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Save Java model class or any other in @{@link SharedPreferences}
     * @param key key associate with data
     * @param object object to be saved
     * @return boolean true= object saved, false = object not saved
     */
    public boolean storeObject(String key, Object object) {
        try {
            String json = gson.toJson(object);
            savePref(key, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get object stored in {@link SharedPreferences}
     * @param key key to be fetched
     * @param mClass Java class eg. User.class, Customer.class, String.class
     * @param <T> Generic return type
     * @return object
     */
    public <T> T getObject(String key, Class<T> mClass) {
        try {
            String json = getPref(key);
            return gson.fromJson(json, mClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
