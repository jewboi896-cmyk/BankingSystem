package com.bank.role;

/*
helper enum to manage what accounts have what roles. the permission system uses this to
check an actions validity

CUSTOMER => means a customer
TELLER => means the person behind the counter
ADMIN => means exec members, system admins, managers, etc
 */
public enum Role {
    CUSTOMER,
    TELLER,
    ADMIN
}
