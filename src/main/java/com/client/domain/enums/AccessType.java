package com.client.domain.enums;

/**
 * @author sdaskaliesku
 */
public enum AccessType {
    Pro, // secret function with `bugs`
    Full, // full access except pro functions
    Basic, // a bit of functions
    NoAccess, // there is no such user id db, due to this, he cannot activate client
    Denied // client should immediately shutdown
}
