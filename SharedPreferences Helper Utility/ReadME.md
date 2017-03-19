# How to use?

Initialise the utility before use as follows
```sh
PreferenceHelper.init(getApplicationContext());
```
Save the key/value pair
```sh
PreferenceHelper.getInstance().savePref("email","amol.p.s.kamble@gmail.com");
```
Save the key/value pair
```sh
PreferenceHelper.getInstance().getPref("email");
```

Save java model class as object
```sh
public class User {
    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
PreferenceHelper.getInstance().storeObject("user",user);
```

Get java model class as object
```sh
User user=PreferenceHelper.getInstance().getObject("user",User.class);
```

Remove all keys
```sh
PreferenceHelper.getInstance().removeAll();
```
# Things to note

  - Gson library is required to store/retrive java model classes as object

