export function camelToSnake (str) {
    return str.replace(/[A-Z]/g, (char, index) => {
        return index == 0 ? char.toLowerCase() : '_' + char.toLowerCase();
    });
}
