-- 将 once_fill.quantity 从整数调整为两位小数精度
ALTER TABLE once_fill
    MODIFY COLUMN quantity DECIMAL(10,2) NOT NULL;
