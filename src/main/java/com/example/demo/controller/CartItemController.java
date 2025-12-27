@PutMapping("/{itemId}")
public CartItem updateItem(
        @PathVariable Long itemId,
        @RequestParam Integer quantity
) {
    return cartItemService.updateItem(itemId, quantity);
}
