package app.snapcard.com;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    ImageView qrcode;
    Button scan;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    new IntentIntegrator(this).initiateScan(); // `this` is the current Activity

        //    qrcode = (ImageView) findViewById(R.id.qrCode);
        qrcode = (ImageView) findViewById(R.id.qrCode);
        scan = (Button) findViewById(R.id.scan);
        result=(TextView) findViewById(R.id.scanresult);

        try {
            Bitmap bitmap = encodeAsBitmap("Sajjad");
            qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan a QR code");

                integrator.initiateScan();

                /*Intent intent = new Intent(
                        "com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);*/
            }
        });

    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 49374) {
            if (resultCode == RESULT_OK) {


                String contents = data.getStringExtra("SCAN_RESULT"); // This will contain your scan result
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");



                result.setText(contents);


            }
        }

    }

    // ZXing Result Handler

    /*public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {


                String contents = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                result.setText(contents);


            }
        }

    }*/
}
