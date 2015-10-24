package net.gongmingqm10.markethacker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.model.Order;
import net.gongmingqm10.markethacker.rest.RestClient;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaymentActivity extends BaseActivity {

    public static final String PARAM_ORDER_ID = "order_id";
    @Bind(R.id.total_price)
    protected TextView totalPriceText;

    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initView();
    }

    private void initView() {
        order = (Order) getIntent().getSerializableExtra(MainActivity.PARAM_ORDER);

        totalPriceText.setText(getString(R.string.format_price, order.getTotalPrice()));
    }

    @OnClick(R.id.confirm_payment)
    protected void confirmPayment() {
        showProgressDialog();

        RestClient.getInstance().getProductService().payOrder(order.getOid(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                dismissProgressDialog();
                if (order.isStatus()) {
                    Intent intent = new Intent(PaymentActivity.this, PaymentCredentialActivity.class);
                    intent.putExtra(PARAM_ORDER_ID, order.getOid());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PaymentActivity.this, R.string.payment_hacking, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Toast.makeText(PaymentActivity.this, getString(R.string.payment_failure), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ProgressDialog progressDialog;

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.is_paying));
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }


}
