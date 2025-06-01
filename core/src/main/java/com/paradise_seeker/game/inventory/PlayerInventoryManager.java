package com.paradise_seeker.game.inventory;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.object.item.Fragment;
import com.paradise_seeker.game.entity.object.item.Item;

/**
 * Class chuyên quản lý kho đồ của người chơi.
 * Tách ra từ class Player để tuân thủ nguyên tắc Single Responsibility (SRP) trong SOLID
 */
public class PlayerInventoryManager {
    private ArrayList<Item> inventory;
    private int inventorySize;
    private int[] collectAllFragments = {0, 0, 0};

    public PlayerInventoryManager() {
        this.inventory = new ArrayList<>();
        this.inventorySize = 18;
    }

    public PlayerInventoryManager(int inventorySize) {
        this.inventory = new ArrayList<>();
        this.inventorySize = inventorySize;
    }

    /**
     * Thêm vật phẩm vào kho đồ
     * @param newItem vật phẩm cần thêm
     */
    public void addItemToInventory(Item newItem, Rectangle playerBounds) {
        if (newItem == null || !newItem.isActive()) return;

        if (newItem.isStackable()) {
            for (Item existingItem : inventory) {
                if (existingItem.canStackWith(newItem)) {
                    int total = existingItem.getCount() + newItem.getCount();
                    if (total <= existingItem.getMaxStackSize()) {
                        existingItem.setCount(total);
                        newItem.setActive(false);
                        return;
                    } else {
                        int remaining = total - existingItem.getMaxStackSize();
                        existingItem.setCount(existingItem.getMaxStackSize());
                        newItem.setCount(remaining);
                    }
                }
            }
        }

        if (inventory.size() < inventorySize && !(newItem instanceof Fragment)) {
            inventory.add(newItem);
            newItem.setActive(false);
        } else if (inventory.size() < inventorySize && newItem instanceof Fragment) {
            Fragment newFragment = (Fragment) newItem;
            inventory.add(newFragment);
            collectAllFragments[newFragment.getFragmentIndex() - 1] = 1;

            if (collectAllFragments[0] == 1 && collectAllFragments[1] == 1 && collectAllFragments[2] == 1) {
                Fragment frag = new Fragment(playerBounds.x, playerBounds.y, playerBounds.width, "items/fragment/frag4.png", 4);

                Iterator<Item> iterator = inventory.iterator();
                while (iterator.hasNext()) {
                    Item item = iterator.next();
                    if (item instanceof Fragment) {
                        iterator.remove();
                    }
                }

                inventory.add(frag);
            }
        } else {
            System.out.println("Inventory is full!");
        }
    }

    /**
     * Kiểm tra số lượng của một vật phẩm trong kho đồ
     * @param itemId ID của vật phẩm cần kiểm tra
     * @return số lượng vật phẩm có trong kho đồ
     */
    public int getItemCount(String itemId) {
        int count = 0;
        for (Item item : inventory) {
            if (item.getName().equals(itemId)) {
                count += item.getCount();
            }
        }
        return count;
    }

    /**
     * Kiểm tra xem kho đồ có còn chỗ trống không
     * @return true nếu còn chỗ trống, false nếu đã đầy
     */
    public boolean hasSpaceLeft() {
        return inventory.size() < inventorySize;
    }

    /**
     * Lấy danh sách vật phẩm trong kho đồ
     * @return danh sách vật phẩm
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    /**
     * Lấy kích thước tối đa của kho đồ
     * @return kích thước tối đa của kho đồ
     */
    public int getInventorySize() {
        return inventorySize;
    }

    /**
     * Lấy trạng thái thu thập các mảnh Fragment
     * @return mảng trạng thái thu thập các mảnh Fragment
     */
    public int[] getCollectAllFragments() {
        return collectAllFragments;
    }

    /**
     * Cập nhật trạng thái thu thập một mảnh Fragment
     * @param index vị trí mảnh (1-3)
     * @param value giá trị (0 hoặc 1)
     */
    public void setFragmentCollected(int index, int value) {
        if (index >= 0 && index < collectAllFragments.length) {
            collectAllFragments[index] = value;
        }
    }

    /**
     * Xóa một vật phẩm khỏi kho đồ
     * @param item vật phẩm cần xóa
     */
    public void removeItem(Item item) {
        inventory.remove(item);
    }
}
