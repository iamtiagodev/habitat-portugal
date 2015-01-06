package Persistence;

import Model.Stock;

import java.sql.*;
import java.util.*;


@SuppressWarnings("UnusedDeclaration")
public class StockRepository implements Map<Integer, Stock> {

    public static final String INSERT_STOCK = "insert into stock(descricao,quantidade, doacao_id) values(?,?,?)";
    public static final String UPDATE_STOCK = "update stock set descricao = ?, quantidade = ?, doacao_id = ? where id_stock = ?";

    public static final String DELETE_STOCK = "delete from stock where id_stock = ?";
    public static final String DELETE_STOCKS = "delete from stock";

    public static final String SELECT_STOCK = "select * from stock where id_stock = ?";
    public static final String SELECT_STOCKS = "select * from stock";

    public static final String COUNT_STOCK = "select count(*) as n from stock";
    private static final String SELECT_IDS = "select id_stock from stock";

    private final String url;
    private final String user;
    private final String password;

    public StockRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public int size() {
        try {
            int count;

            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            try (ResultSet resultSet = statement.executeQuery(COUNT_STOCK)) {
                if (resultSet.next())
                    count = resultSet.getInt("n");
                else
                    count = -1;
            } finally {
                statement.close();
                connection.close();
            }

            return count;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_STOCK);

            statement.setInt(1, (int) key);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            } finally {
                statement.close();
                connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsKey(((Stock) value).getId_stock());
    }

    @Override
    public Stock get(Object key) {
        Stock stock = null;
        try {

            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_STOCK);

            statement.setInt(1,(int) key);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    stock = new Stock();
                    stock.setId_stock((int) key);
                    stock.setDescricao(result.getString("descricao"));
                    stock.setQuantidade(result.getString("quantidade"));
                    stock.setDoacao_id(result.getInt("doacao_id"));
                }
            } finally {
                statement.close();
                connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stock;
    }

    public Stock put(Integer key, Stock value) {
        String query;
        int autoGeneratedKeys;
        boolean isUpdate;
        if (key < 0) {
            isUpdate = false;
            query = INSERT_STOCK;
            autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;
        }
        else {
            isUpdate = true;
            query = UPDATE_STOCK;
            autoGeneratedKeys = Statement.NO_GENERATED_KEYS;
        }

        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(query,autoGeneratedKeys);

            statement.setString(1, (value.getDescricao()));
            statement.setString(2, (value.getQuantidade()));
            statement.setInt(3, (value.getDoacao_id()));

            if (isUpdate) {
                statement.setInt(4, key);
            }

            statement.executeUpdate();

            try {
                if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
                    ResultSet keys = statement.getGeneratedKeys();
                    if (keys != null && keys.next()) {
                        value.setId_stock(keys.getInt(1));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                statement.close();
                connection.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    @Override
    public Stock remove(Object key) {
        Stock s = get(key);
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(DELETE_STOCK);

            statement.setInt(1, (int) key);

            try {
                statement.executeUpdate();
            } finally {
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {
        for (Stock stock: m.values()) {
            put(stock.getId_stock(),stock);
        }
    }

    @Override
    public void clear() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(DELETE_STOCKS);
            } finally {
                connection.close();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> s = new HashSet<>();

        try {

            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(SELECT_IDS);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    s.add(result.getInt(1));
                }
            } finally {
                statement.close();
                connection.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    @Override
    public Collection<Stock> values() {
        ArrayList<Stock> r = new ArrayList<>();
        try {
            Stock stock;

            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_STOCKS);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    stock = new Stock();

                    stock.setId_stock(result.getInt("id_stock"));
                    stock.setDescricao(result.getString("descricao"));
                    stock.setQuantidade(result.getString("quantidade"));
                    stock.setDoacao_id(result.getInt("doacao_id"));

                    r.add(stock);
                }
            }  finally {
                statement.close();
                connection.close();
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return r;
    }

    public Set<Entry<Integer, Stock>> entrySet() {
        return null;
    }
}
