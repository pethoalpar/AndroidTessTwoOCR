<h1>Android TessTwo OCR example</h1>

<h3>Add dependency to app/build.gradle</h3>

```gradle
dependencies {
...
compile 'com.rmtheis:tess-two:5.4.1'
...
}
```

<h3>Get text from bitmap</h3>

```java
private String getText(Bitmap bitmap){
    try{
      tessBaseAPI = new TessBaseAPI();
    }catch (Exception e){
      Log.e(TAG, e.getMessage());
    }
    tessBaseAPI.init(DATA_PATH,"eng");
    tessBaseAPI.setImage(bitmap);
    String retStr = "No result";
    try{
      retStr = tessBaseAPI.getUTF8Text();
    }catch (Exception e){
      Log.e(TAG, e.getMessage());
    }
    tessBaseAPI.end();
    return retStr;
}
```
