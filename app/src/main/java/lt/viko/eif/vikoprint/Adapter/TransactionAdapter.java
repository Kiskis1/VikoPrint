package lt.viko.eif.vikoprint.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lt.viko.eif.vikoprint.Model.Transaction;
import lt.viko.eif.vikoprint.R;

public class TransactionAdapter
        extends ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {

    private Interaction interaction;

    private TextView mDate;
    private TextView mPageCount;
    private TextView mPoints;

    public TransactionAdapter(Interaction interaction) {
        super(new TransactionDC());
        this.interaction = interaction;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycle_view, parent, false),
                interaction);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void swapData(List<Transaction> data) {
        submitList(data);
        notifyDataSetChanged();

    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Interaction interaction;

        TransactionViewHolder(View inflate, Interaction interaction) {
            super(inflate);
            this.interaction = interaction;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            Transaction clicked = getItem(getAdapterPosition());
            switch (v.getId()) {
                //TODO handle clicks
                default:
            }
        }

        public void bind(Transaction item) {
            //TODO use itemView and set data
            mDate = itemView.findViewById(R.id.recycler_dateView);
            mPageCount = itemView.findViewById(R.id.recycler_pagesView);
            mPoints = itemView.findViewById(R.id.recycler_pointsView);

            mDate.setText(""+item.getDate());
            mPageCount.setText(""+item.getPageCount());
            mPoints.setText(""+item.getPoints());
        }
    }

    public interface Interaction {
    }

    private static class TransactionDC extends DiffUtil.ItemCallback<Transaction> {
        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem,
                                       @NonNull Transaction newItem) {
            //TODO "not implemented"
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem,
                                          @NonNull Transaction newItem) {
            //TODO "not implemented"
            return false;
        }
    }
}