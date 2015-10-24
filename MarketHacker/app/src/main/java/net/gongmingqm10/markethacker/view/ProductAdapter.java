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

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void populate(Product product) {
            String imageUrl = ApiEnvironment.BASE_URL + product.getAvatar().getUrl();
            Picasso.with(context).load(imageUrl).into(productImage);
            productPrice.setText(context.getResources().getString(R.string.format_price, product.getPrice()));
            productDisc.setText(context.getResources().getString(R.string.format_desc, product.getName(), product.getDesc()));
        }
    }


}
