package com.example.quanlykhachsan.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlykhachsan.Booking_Model.Model_BookingService;
import com.example.quanlykhachsan.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Service_Adapter extends BaseAdapter {
    private Context context;
    private List<Model_Service> serviceList;
    private LayoutInflater inflater;
    private Map<String, Integer> selectedServices = new HashMap<>();
    private List<String> selectedServiceIds = new ArrayList<>();
    private OnServiceSelectedListener listener;
    private List<Model_BookingService> ServiceID = new ArrayList<>();
    public interface OnServiceSelectedListener {
        void onServiceSelected(double totalServicePrice, List<String> selectedIds);
    }

    public Service_Adapter(Context context, List<Model_Service> serviceList, OnServiceSelectedListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customlistview_service, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Model_Service service = serviceList.get(position);
        holder.name.setText(service.getName());
        holder.price.setText(String.format("%,d VNĐ", service.getPrice()));

        // Khôi phục trạng thái
        int quantity = selectedServices.getOrDefault(service.getId(), 0);
        holder.Quantity.setText(String.valueOf(quantity));
        holder.checkBox.setChecked(selectedServiceIds.contains(service.getId()));

        holder.increment.setOnClickListener(v -> updateQuantity(service, holder, 1));
        holder.decrement.setOnClickListener(v -> updateQuantity(service, holder, -1));

//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                Model_BookingService bookingService = new Model_BookingService();
//                bookingService.setServiceID(service.getId());
//                ServiceID.add(bookingService);
//                if (!selectedServiceIds.contains(service.getId())) {
//                    selectedServiceIds.add(service.getId());
//                }
//                if (selectedServices.getOrDefault(service.getId(), 0) == 0) {
//                    selectedServices.put(service.getId(), 1);
//                    holder.quantity.setText("1");
//                }
//            } else {
//                ServiceID.removeIf(s -> s.getServiceID().equals(service.getId()));
//                selectedServiceIds.remove(service.getId());
//                selectedServices.remove(service.getId());
//                holder.quantity.setText("0");
//            }
//            updateTotalServicePrice();
//        });
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Model_BookingService bookingService = findOrCreateBookingService(service.getId());

            if (isChecked) {
                if (!selectedServiceIds.contains(service.getId())) {
                    selectedServiceIds.add(service.getId());
                }
                if (selectedServices.getOrDefault(service.getId(), 0) == 0) {
                    selectedServices.put(service.getId(), 1);
                    holder.Quantity.setText("1");
                    bookingService.setQuantity(1);  // Thiết lập số lượng ban đầu là 1
                }
            } else {
                ServiceID.remove(bookingService);
                selectedServiceIds.remove(service.getId());
                selectedServices.remove(service.getId());
                holder.Quantity.setText("0");
            }
            updateTotalServicePrice();
        });

        return convertView;
    }
    public List<Model_BookingService> getSelectedServicesall() {
        return ServiceID;
    }
//    private void updateQuantity(Model_Service service, ViewHolder holder, int change) {
//        int quantity = Integer.parseInt(holder.quantity.getText().toString()) + change;
//        if (quantity >= 0) {
//            holder.quantity.setText(String.valueOf(quantity));
//            if (quantity > 0) {
//                selectedServices.put(service.getId(), quantity);
//                if (!selectedServiceIds.contains(service.getId())) {
//                    selectedServiceIds.add(service.getId());
//                }
//                holder.checkBox.setChecked(true);
//            } else {
//                selectedServices.remove(service.getId());
//                selectedServiceIds.remove(service.getId());
//                holder.checkBox.setChecked(false);
//            }
//            updateTotalServicePrice();
//        }
//    }
private void updateQuantity(Model_Service service, ViewHolder holder, int change) {
    int Quantity = Integer.parseInt(holder.Quantity.getText().toString()) + change;
    if (Quantity >= 0) {
        holder.Quantity.setText(String.valueOf(Quantity));

        // Tìm hoặc tạo mới Model_BookingService với ServiceID
        Model_BookingService bookingService = findOrCreateBookingService(service.getId());

        if (Quantity > 0) {
            bookingService.setQuantity(Quantity);  // Cập nhật số lượng
            selectedServices.put(service.getId(), Quantity);

            if (!selectedServiceIds.contains(service.getId())) {
                selectedServiceIds.add(service.getId());
            }
            holder.checkBox.setChecked(true);
        } else {
            ServiceID.remove(bookingService);  // Xóa khỏi danh sách nếu số lượng là 0
            selectedServices.remove(service.getId());
            selectedServiceIds.remove(service.getId());
            holder.checkBox.setChecked(false);
        }
        updateTotalServicePrice();
    }
}
    private Model_BookingService findOrCreateBookingService(String serviceID) {
        for (Model_BookingService bookingService : ServiceID) {
            if (bookingService.getServiceID().equals(serviceID)) {
                return bookingService;
            }
        }
        // Tạo mới nếu không tồn tại
        Model_BookingService newService = new Model_BookingService();
        newService.setServiceID(serviceID);
        newService.setQuantity(0);  // Bắt đầu với số lượng 0
        ServiceID.add(newService);
        return newService;
    }


    private void updateTotalServicePrice() {
        double totalServicePrice = 0;
        for (Map.Entry<String, Integer> entry : selectedServices.entrySet()) {
            Model_Service service = getServiceById(entry.getKey());
            if (service != null) {
                totalServicePrice += service.getPrice() * entry.getValue();
            }
        }
        if (listener != null) {
            listener.onServiceSelected(totalServicePrice, selectedServiceIds);
        }
    }

    private Model_Service getServiceById(String id) {
        for (Model_Service service : serviceList) {
            if (service.getId().equals(id)) {
                return service;
            }
        }
        return null;
    }

    public Map<String, Integer> getSelectedServices() {
        return selectedServices;
    }

    public List<String> getSelectedServiceIds() {
        return selectedServiceIds;
    }

    private static class ViewHolder {
        TextView name, price, Quantity;
        MaterialCheckBox checkBox;
        MaterialButton increment, decrement;

        ViewHolder(View view) {
            checkBox = view.findViewById(R.id.check);
            name = view.findViewById(R.id.tendichvu);
            price = view.findViewById(R.id.giadv);
            Quantity = view.findViewById(R.id.soLuong);
            increment = view.findViewById(R.id.icontang);
            decrement = view.findViewById(R.id.icongiam);
        }
    }
}