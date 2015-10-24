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

    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credential);
        orderId = getIntent().getStringExtra(PaymentActivity.PARAM_ORDER_ID);
        initView();
    }

    private void initView() {
        Bitmap myBitmap = QRCode.from("markethacker://"+orderId).bitmap();
        qrImage.setImageBitmap(myBitmap);
    }
}
