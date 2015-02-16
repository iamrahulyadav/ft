package jp.sharakova.android.emoji;

import android.app.Activity;
import android.os.Bundle;

public class EmojiTextViewSampleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        EmojiTextView emojiTextView = (EmojiTextView)findViewById(R.id.EmojiTextView1);
        
       // String emojiText = "�G�����e�X�g%%i:3%%�A����ǂ́A%%i:85%%";
       // emojiTextView.setEmojiText(emojiText);
    }
}