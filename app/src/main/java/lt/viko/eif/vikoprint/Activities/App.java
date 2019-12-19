package lt.viko.eif.vikoprint.Activities;

import android.app.Application;

import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

    }
}
