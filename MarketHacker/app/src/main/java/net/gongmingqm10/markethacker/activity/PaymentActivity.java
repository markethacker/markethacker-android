package net.gongmingqm10.markethacker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import net.gongmingqm10.markethacker.R;

import butterknife.Bind;
import butterknife.OnClick;

public class PaymentActivity extends BaseActivity {

    @Bind(R.id.total_price)
    protected TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initView();
    }

    private void initView() {
        float totalPrice = getIntent().getFloatExtra(MainActivity.PARAM_TOTAL, 0f);

        totalPriceText.setText(getString(R.string.format_price, totalPrice));
    }

    @OnClick(R.id.confirm_payment)
    protected void confirmPayment() {
        startActivity(new Intent(this, PaymentCredentialActivity.class));
        finish();
    }

}
