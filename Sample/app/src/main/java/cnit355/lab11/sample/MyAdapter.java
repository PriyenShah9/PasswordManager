package cnit355.lab11.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Websites> web;

    public MyAdapter(Context context, ArrayList<Websites> web, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.web = web;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Websites webPos = web.get(position);
        holder.website.setText(webPos.Name);


    }

    @Override
    public int getItemCount() {
        return web.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView website;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            website = (TextView) itemView.findViewById(R.id.textView);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            recyclerViewInterface.onItemClick(position);
                        }
                    }




                }
            });
        }
    }
}