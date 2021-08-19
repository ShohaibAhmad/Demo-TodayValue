package com.promoteprovider.demotodayvalue.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.promoteprovider.demotodayvalue.databinding.ItemContainerUserBinding;
import com.promoteprovider.demotodayvalue.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContainerUserBinding itemContainerUserBinding=ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding=itemContainerUserBinding;
        }

        void setUserData(User user){
            binding.textName.setText(user.name);
            binding.textName1.setText(user.name1);
            binding.textEmail.setText(user.email);
//            binding.imageProfile.setImageBitmap(getUserImage(user.image));
        }

    }
//    private Bitmap getUserImage(String encodedImage){
//        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }
}
