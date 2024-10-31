package com.example.quanlykhachsan.KhuyenMai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykhachsan.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KhuyenMai_Adapter extends RecyclerView.Adapter<KhuyenMai_Adapter.PromotionViewHolder> {

    private List<Model_KhuyenMai> promotions;
    private OnItemSelectedListener onItemSelectedListener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private boolean showRadioButton = true;

    public void setRadioButtonVisibility(boolean visible) {
        this.showRadioButton = visible;
        notifyDataSetChanged();
    }
    public KhuyenMai_Adapter(List<Model_KhuyenMai> promotions, OnItemSelectedListener listener) {
        this.promotions = promotions;
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customlistview_khuyenmai, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        holder.bind(promotions.get(position), position);

        if (holder.radioButton != null) {
            holder.radioButton.setVisibility(showRadioButton ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView promotionName;
        private TextView Gia;
        private TextView tvExpiry;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.rbPromotion);
            promotionName = itemView.findViewById(R.id.tvPromotionName);
            Gia=itemView.findViewById(R.id.tvgia);
            tvExpiry=itemView.findViewById(R.id.tvExpiry);
        }

        public void bind(final Model_KhuyenMai promotion, final int position) {
            promotionName.setText(promotion.getName());
            radioButton.setChecked(position == selectedPosition);
            tvExpiry.setText("Sử dụng trước : " +promotion.getEndDate());
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedAmount = currencyFormat.format(promotion.getDiscountAmount());
            Gia.setText(formattedAmount);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleItemClick(position);
                }
            };

            itemView.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);
        }

        private void handleItemClick(int position) {
            if (selectedPosition != position) {
                int previousSelected = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(promotions.get(position));
                }
            }
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Model_KhuyenMai promotion);
    }
}