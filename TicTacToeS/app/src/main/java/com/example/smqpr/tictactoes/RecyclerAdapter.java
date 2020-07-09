package com.example.smqpr.tictactoes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<User> users;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView scoreTextView;

        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            scoreTextView = v.findViewById(R.id.scoreTextView);
        }

        public void setName(String name) {
            nameTextView.setText(name);
        }

        public void setScore(String score) {
            scoreTextView.setText(score);
        }
    }

    public RecyclerAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setName(users.get(position).getPlayer());
        holder.setScore(String.valueOf(users.get(position).getRecord()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}