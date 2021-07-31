import {snakeToCamel} from "./converter/snakeToCamel";

export function transform<T> (json, type: { new(): T ;} ) {
    Object.keys(json).forEach(key => renameColumnName(json, key));
    return Object.assign(new type(), json);
}

function renameColumnName(json, oldKey) {
    const newKey = snakeToCamel(oldKey);
    if(oldKey === newKey) {
        return;
    }

    json[newKey] = json[oldKey];
    delete json[oldKey];
}
