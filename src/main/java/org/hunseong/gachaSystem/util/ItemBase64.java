package org.hunseong.gachaSystem.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemBase64 {

    public ItemStack ItemDecodingBase64(String encodedItem) {
        byte[] data = Base64.getDecoder().decode(encodedItem);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String ItemEncodingBase64(ItemStack item) {
        //아이템 인코딩 try-with-resources 방식이라 dataOutput.close(); 사용 안해도 됨(자동으로 닫아줌)
        String encodedItem = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            //BASE64 인코딩
            encodedItem = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            return encodedItem;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
