package com.example.community_stack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class TopicsAdapter  extends RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder> {
    @NonNull
    private final String[] topics;
    private Context context;

    public TopicsAdapter(@NotNull String[] data)
    {
        this.topics = data;

    }
    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.topics_item_view, parent, false);
        context = view.getContext();
        return new TopicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsViewHolder holder, int position) {
        String topic = topics[position];
        holder.textView.setText(topic);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SuggestionActivity.class);
                intent.putExtra("Topic", topics[position]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return topics.length;
    }


    public class TopicsViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public TopicsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.topics_item_text);

        }

    }


}
