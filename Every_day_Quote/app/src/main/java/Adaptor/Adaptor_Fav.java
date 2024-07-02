package Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welcomepage.R;

import java.util.List;

public class Adaptor_Fav extends RecyclerView.Adapter<Adaptor_Fav.ViewHolder> {

    private List<String> favoriteQuotesList;

    public Adaptor_Fav(List<String> favoriteQuotesList) {
        this.favoriteQuotesList = favoriteQuotesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_quote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String quote = favoriteQuotesList.get(position);
        holder.bind(quote);
    }

    @Override
    public int getItemCount() {
        return favoriteQuotesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Fav_text);
        }

        public void bind(String quote) {
            textView.setText(quote);
        }
    }
}
