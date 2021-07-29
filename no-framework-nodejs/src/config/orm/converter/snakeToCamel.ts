export function snakeToCamel (str) {
    return str.replace(/_([a-z])/g, (char) =>  char[1].toUpperCase());
}
