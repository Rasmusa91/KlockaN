package com.superteam.klockan;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;

import java.util.Locale;

/**
 * Created by Rasmus on 2017-01-01.
 */

public class TextToSpeechHandler
{
    private TextToSpeech m_TextToSpeech;

    public TextToSpeechHandler(Context p_Context)
    {
        m_TextToSpeech = new TextToSpeech(p_Context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    m_TextToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speak(String p_Text)
    {
        if(m_TextToSpeech.isSpeaking()) {
            return;
        }

        m_TextToSpeech.speak(p_Text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
