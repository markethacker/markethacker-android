package net.gongmingqm10.markethacker.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.model.Product;
import net.gongmingqm10.markethacker.rest.ApiEnvironment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private List<Product> products;
    private ProductQuantityListener quantityListener;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setQuantityListener(ProductQuantityListener quantityListener) {
        this.quantityListener = quantityListener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.populate(getItem(position));
        return convertView;
    }


    protected class ViewHolder{

        @Bind(R.id.product_image)
        protected ImageView productImage;
        @Bind(R.id.product_price)
        protected TextView productPrice;
        @Bind(R.id.product_desc)
        protected TextView productDisc;
        @Bind(R.id.product_quantity)
        protected TextView productQuantity;
        @Bind(R.id.product_count_add)
        protected ImageView addQuantityBtn;
        @Bind(R.id.product_count_minus)
        protected ImageView minusQuantityBtn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void populate(final Product product) {
            String imageUrl = ApiEnvironment.BASE_URL + product.getAvatar().getUrl();
            Picasso.with(context).load(imageUrl).into(productImage);
            productPrice.setText(context.getResources().getString(R.string.format_price, product.getPrice()));
            productDisc.setText(context.getResources().getString(R.string.format_desc, product.getName(), product.getDesc()));

            final int quantity = product.getQuantity();
            productQuantity.setText(String.valueOf(quantity));

            addQuantityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityListener != null) {
                        quantityListener.addProduct(product.getPid());
                    }
                }
            });

            minusQuantityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantityListener != null) {
                        quantityListener.minusProduct(product.getPid());
                    }
                }
            });


        }

    }

    public interface ProductQuantityListener {
        void addProduct(String pid);
        void minusProduct(String pid);
    }


}
