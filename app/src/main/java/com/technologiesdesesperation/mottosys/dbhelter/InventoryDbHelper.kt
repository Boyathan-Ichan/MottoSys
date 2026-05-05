package com.technologiesdesesperation.mottosys.dbhelter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InventoryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MottoSys_Internal.db"
        const val DATABASE_VERSION = 1

        // Sentencias SQL
        private const val CREATE_PRODUCTS = """
            CREATE TABLE products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                barcode TEXT,
                name TEXT NOT NULL,
                price_buy REAL,
                price_sell REAL NOT NULL,
                stock INTEGER DEFAULT 0,
                category TEXT,
                user_email TEXT NOT NULL
            )
        """

        private const val CREATE_SALES = """
            CREATE TABLE sales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                total REAL NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                user_email TEXT NOT NULL
            )
        """

        private const val CREATE_SALE_DETAILS = """
            CREATE TABLE sale_details (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sale_id INTEGER,
                product_id INTEGER,
                quantity INTEGER,
                price_at_time REAL,
                FOREIGN KEY(sale_id) REFERENCES sales(id)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_PRODUCTS)
        db.execSQL(CREATE_SALES)
        db.execSQL(CREATE_SALE_DETAILS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // En una app de este tipo, si actualizas la versión, mejor no borrar datos
        // Pero para desarrollo, puedes usar:
        // db.execSQL("DROP TABLE IF EXISTS products")
        // onCreate(db)
    }

    /**
     * LA PUERTA TRASERA (Nuke)
     * Borra el contenido de todas las tablas sin borrar el archivo de la DB
     */
    fun nukeDatabase() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("DELETE FROM sale_details")
            db.execSQL("DELETE FROM sales")
            db.execSQL("DELETE FROM products")
            // Opcional: Reiniciar los contadores de ID autoincrementales
            db.execSQL("DELETE FROM sqlite_sequence WHERE name IN ('products', 'sales', 'sale_details')")
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}