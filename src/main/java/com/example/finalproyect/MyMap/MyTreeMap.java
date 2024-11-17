package com.example.finalproyect.MyMap;

import com.example.finalproyect.UserTree.Node;
import com.example.finalproyect.UserTree.User;
import com.example.finalproyect.UserTree.UserTree;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.io.*;
import java.util.*;


public class MyTreeMap<K extends Comparable<K>, V> implements Iterable<K> {

    private Set<K> keys;
    private Set<Entry<K, V>> entries;

    public MyTreeMap() {
        keys = new TreeSet<>();
        entries = new TreeSet<>((a, b) -> a.getKey().compareTo(b.getKey()));
    }

    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entrySet()) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }


    public static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    // Método para añadir una entrada al mapa
    public void put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        entries.add(entry);
        keys.add(key);
    }

    // Método para obtener el valor asociado a una clave
    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null; // Si no se encuentra la clave, retornamos null
    }

    // Retornar el conjunto de claves
    public Set<K> keySet() {
        return keys;
    }

    // Implementar el iterador para recorrer solo las claves
    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    // Método para imprimir el mapa (clave = valor)
    public void printMap() {
        for (Entry<K, V> entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    // Obtener el tamaño del mapa
    public int size() {
        return entries.size();
    }

    // Método para eliminar una entrada dado su clave
    public boolean remove(K key) {
        Iterator<Entry<K, V>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();  // Elimina la entrada de `entries` de forma segura
                keys.remove(key);    // Elimina la clave del conjunto `keys`
                return true;
            }
        }
        return false;  // Si no se encuentra la clave, retornamos false
    }

    // Método para obtener el conjunto de entradas (clave-valor)
    public Set<Entry<K, V>> entrySet() {
        return entries;
    }
    public static MyTreeMap<User,String> loadFromFile(String route) {
        try {
            MyTreeMap<User, String> userMap = new MyTreeMap<>();
            // Lee el archivo línea por línea
            BufferedReader reader = new BufferedReader(new FileReader(route));
            String line;
            while ((line = reader.readLine()) != null) {
                // Suponemos que cada línea tiene el formato: User: valor, descripcion
                String[] parts = line.split(",");  // Divide la línea en 2 partes, la primera parte es el valor y la segunda es la descripción

                if (parts.length == 2) {
                    // Extraemos los datos
                    String userValue = parts[0].split(":")[1].trim();  // Extrae el valor del usuario
                    String description = parts[1].trim();  // Extrae la descripción

                    // Creamos el objeto User
                    User user = new User(null, null, null, userValue);

                    // Insertamos el objeto User y su valor asociado al mapa
                    userMap.put(user, description);
                }
            }
            reader.close();
            return userMap;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Método para guardar el mapa en un archivo
    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
            for (Entry<K, V> entry : entries) {
                writer.write(entry.getKey() + ", " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromJson(String fileName) {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();

            // Asegúrate de que el tipo esté bien definido
            Type type = new TypeToken<UserTree>() {}.getType();

            UserTree loadedData = gson.fromJson(reader, type);

            if (loadedData != null) {
                System.out.println("Datos cargados correctamente: " + loadedData);
                System.out.println(loadedData);
            } else {
                System.err.println("Error al cargar datos del archivo JSON.");
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }


}


