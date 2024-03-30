package rw.org.nkurage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ImageView imgNtakomisiyo;
    ImageView errorImg;
    ImageView errorReflesh;
    TextView errorTxt;
    WebView webView;
    Button donateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        webView = findViewById(R.id.webView);
        donateBtn = findViewById(R.id.donateBtn);
        imgNtakomisiyo = findViewById(R.id.imgNtakomisiyo);
        errorImg = findViewById(R.id.errorImg);
        errorReflesh = findViewById(R.id.errorReflesh);
        errorTxt = findViewById(R.id.errorTxt);
        errorReflesh.setVisibility(View.GONE);
        errorTxt.setVisibility(View.GONE);
        errorImg.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("https://www.nkurage.org.rw/");

        Animation zoomInOut = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInOut.setDuration(1000);
        zoomInOut.setRepeatCount(Animation.INFINITE);
        zoomInOut.setRepeatMode(Animation.REVERSE);
        imgNtakomisiyo.startAnimation(zoomInOut);

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                imgNtakomisiyo.startAnimation(zoomInOut);
                imgNtakomisiyo.setVisibility(View.VISIBLE);

                errorImg.setVisibility(View.GONE);
                errorReflesh.setVisibility(View.GONE);
                errorTxt.setVisibility(View.GONE);
            }

            @Override
            public boolean  shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("whatsapp://")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    Toast.makeText(HomeActivity.this, "Nkurage on WhatsApp...", Toast.LENGTH_SHORT).show();
                    startActivity(browserIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://www.youtube.com")) {
                    Intent ytIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    Toast.makeText(HomeActivity.this, "Programmer DATCH on YouTube", Toast.LENGTH_SHORT).show();
                    startActivity(ytIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://play.google.com/")) {
                    Intent psIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    Toast.makeText(HomeActivity.this, "NtaKomisiyo App on PlayStore", Toast.LENGTH_SHORT).show();
                    startActivity(psIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://m.facebook.com")) {
                    Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    Toast.makeText(HomeActivity.this, "Programmer DATCH on Facebook", Toast.LENGTH_SHORT).show();
                    startActivity(fbIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://twitter")) {
                    Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    Toast.makeText(HomeActivity.this, "Programmer DATCH on Twitter", Toast.LENGTH_SHORT).show();
                    startActivity(twitterIntent);
                    return true;

                }
                else if (url != null && url.startsWith("https://www.instagram.com")) {
                    Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    Toast.makeText(HomeActivity.this, "Programmer DATCH on Instagram", Toast.LENGTH_SHORT).show();
                    startActivity(twitterIntent);
                    return true;

                }
                else {
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgNtakomisiyo.clearAnimation();
                imgNtakomisiyo.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected())
                {
                    Toast.makeText(HomeActivity.this, "Network Disconnected!", Toast.LENGTH_LONG).show();
                    errorTxt.setText("Network Disconnected!, Connect again & Reflesh");
                }
                else
                {
                    errorTxt.setText("Page Loading Failed!, Check Network & Reflesh");
                }

                errorImg.setVisibility(View.VISIBLE);
                errorReflesh.setVisibility(View.VISIBLE);
                errorTxt.setVisibility(View.VISIBLE);
                errorReflesh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentLink = view.getUrl();
                        webView.loadUrl(currentLink);
                    }
                });
            }

        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (keyEvent.getAction() == keyEvent.ACTION_DOWN) {
                    if(i == KeyEvent.KEYCODE_BACK)
                    {
                        if (webView.canGoBack()) {
                            webView.goBack();
                        }
                        else
                        {
                            webView.loadUrl("https://www.nkurage.org.rw/");
                        }
                    }
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                HomeActivity.this.filePathCallback = filePathCallback;
                showFileChooser();
                return true;
            }
        });

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donate = new Intent(HomeActivity.this, DonateActivity.class);
                startActivity(donate);
                finish();
            }
        });


    }

    private static final int FILE_CHOOSER_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> filePathCallback;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose Image");
        startActivityForResult(chooser, FILE_CHOOSER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            filePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
        } else {
            filePathCallback.onReceiveValue(null);
        }

        filePathCallback = null;
        super.onActivityResult(requestCode, resultCode, data);
    }

}