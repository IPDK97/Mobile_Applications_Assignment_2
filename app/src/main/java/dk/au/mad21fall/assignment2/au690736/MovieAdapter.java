package dk.au.mad21fall.assignment2.au690736;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Recycler View built using demo-code from the labs
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface IMovieItemClickedListener{
        void onMovieClicked(int index);
    }

    private final IMovieItemClickedListener listener;
    private ArrayList<Movie> movieList;

    public MovieAdapter(IMovieItemClickedListener listener){
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMovieList(ArrayList<Movie> lists){
        movieList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.txtName.setText(movieList.get(position).getName());
        holder.txtRating.setText(String.valueOf(movieList.get(position).getUserRating()));
        holder.txtYear.setText(String.valueOf(movieList.get(position).getYear()));
        String genre = movieList.get(position).getGenre();
        switch (genre) {
            case "Action":
                holder.imgIcon.setImageResource(R.drawable.ic_action);
                break;
            case "Comedy":
                holder.imgIcon.setImageResource(R.drawable.ic_comedy);
                break;
            case "Drama":
                holder.imgIcon.setImageResource(R.drawable.ic_drama);
                break;
            case "Horror":
                holder.imgIcon.setImageResource(R.drawable.ic_horror);
                break;
            case "Romance":
                holder.imgIcon.setImageResource(R.drawable.ic_romance);
                break;
            case "Western":
                holder.imgIcon.setImageResource(R.drawable.ic_western);
                break;
            default:
                holder.imgIcon.setImageResource(R.drawable.ic_resource_default);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgIcon;
        TextView txtName;
        TextView txtRating;
        TextView txtYear;

        IMovieItemClickedListener listener;

        public MovieViewHolder(@NonNull View itemView, IMovieItemClickedListener movieItemClickedListener) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgGenre);
            txtName = itemView.findViewById(R.id.txtName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtYear = itemView.findViewById(R.id.txtYear);
            listener = movieItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onMovieClicked(getBindingAdapterPosition());
        }
    }
}