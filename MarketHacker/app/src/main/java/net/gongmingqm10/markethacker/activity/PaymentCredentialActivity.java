package net.gongmingqm10.markethacker.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;
import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.rest.ApiEnvironment;

import butterknife.Bind;

public class PaymentCredentialActivity extends BaseActivity {

    @Bind(R.id.qr_image)
    protected ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credential);

        initView();
    }

    private void initView() {
        Bitmap myBitmap = QRCode.from(ApiEnvironment.BASE_URL).bitmap();
        qrImage.setImageBitmap(myBitmap);
    }
}
