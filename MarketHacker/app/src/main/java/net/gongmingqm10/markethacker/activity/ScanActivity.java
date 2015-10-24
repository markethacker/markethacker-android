package net.gongmingqm10.markethacker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.model.Product;
import net.gongmingqm10.markethacker.rest.RestClient;
import net.gongmingqm10.markethacker.view.CameraPreview;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ScanActivity extends BaseActivity {

    public static final String PARAM_PRODUCT = "product";

    static {
        System.loadLibrary("iconv");
    }

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private ImageScanner scanner;

    @Bind(R.id.camera_preview)
    protected FrameLayout framePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        initView();

        startPreview();
    }

    private void startPreview() {
        Toast.makeText(this, "扫描中...", Toast.LENGTH_SHORT).show();

        mCamera.setPreviewCallback(previewCb);
        mCamera.startPreview();
        mCamera.autoFocus(autoFocusCB);

    }

    private void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        framePreview.addView(mPreview);


    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    Toast.makeText(ScanActivity.this, sym.getData(), Toast.LENGTH_LONG).show();

                    showProgressDialog();
                    String productId = sym.getData();
                    requestProductDetail(productId);
                }
            }
        }
    };

    private ProgressDialog progressDialog;

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, null, getString(R.string.is_getting_product_details));
    }

    private void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void requestProductDetail(String productId) {
        RestClient.getInstance().getProductService()
                .getProduct(productId, new Callback<Product>() {
                    @Override
                    public void success(Product product, Response response) {
                        Intent intent = new Intent();
                        intent.putExtra(PARAM_PRODUCT, product);
                        setResult(RESULT_OK, intent);

                        dismissDialog();
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(ScanActivity.this, getString(R.string.get_product_detail_failing), Toast.LENGTH_SHORT).show();
                        dismissDialog();
                        finish();
                    }
                });
    }
}
