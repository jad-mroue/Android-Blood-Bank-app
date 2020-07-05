package com.example.databaselogin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Donors> dl;
    private Context context;

    public SearchAdapter(List<Donors> dl, Context context) {
        this.dl = dl;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String s = "Name " + dl.get(position).getName();
        s += "\nCity: " + dl.get(position).getCity() + "\nBlood Type: " + dl.get(position).getBld();
        holder.message.setText(s);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+dl.get(position).getNumber()));
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ((Activity) context).startActivity(intent);
                }
                else{
                    ((Activity) context).requestPermissions(new String[]{READ_EXTERNAL_STORAGE},401);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      TextView message;
      ImageView imageView,call;
        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textView4);
            call = itemView.findViewById(R.id.imageView3);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}
