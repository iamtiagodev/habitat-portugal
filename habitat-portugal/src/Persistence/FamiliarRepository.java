package Persistence;

import Model.Familiar;

import java.sql.*;
import java.util.*;

/**
 * @author davide on 06/12/14.
 */

@SuppressWarnings("UnusedDeclaration")
public class FamiliarRepository implements Map<Integer, Familiar> {

    private static final String INSERT_FAMILIAR = "insert into pessoa (nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade, candidatura_id) values (?,?,?,?,?,?,?)";
    private static final String UPDATE_FAMILIAR = "update pessoa set nome = ?, parentesco = ?, data_nascimento = ?, estado_civil = ?, ocupacao = ?, escolaridade = ?, candidatura_id = ? where id_pessoa = ?";

    private static final String SELECT_FAMILIAR = "select nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade, candidatura_id from pessoa where id_pessoa = ?";
    private static final String SELECT_FAMILIARES = "select id_pessoa, nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade, candidatura_id from pessoa";
    private static final String SELECT_BY_CANDIDATURA = "select id_pessoa, nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade, candidatura_id from pessoa where candidatura_id = ?";

    private static final String DELETE_FAMILIAR = "delete from pessoa where id_pessoa = ?";
    private static final String DELETE_FAMILIARES = "delete from pessoa";

    private static final String COUNT_FAMILIARES = "select count(*) as n from pessoa";
    private static final String SELECT_IDS = "select id_pessoa from pessoa";

    private final String url;
    private final String user;
    private final String password;

    public FamiliarRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public int size() {
        try {
            int count;

            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();

            try (ResultSet resultSet = statement.executeQuery(COUNT_FAMILIARES)) {
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
            PreparedStatement statement = connection.prepareStatement(SELECT_FAMILIAR);

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
        return containsKey(((Familiar) value).getId());
    }

    @Override
    public Familiar get(Object key) {
        Familiar familiar = null;
        try {

            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_FAMILIAR);

            statement.setInt(1, (int) key);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    familiar = new Familiar();
                    familiar.setId((int)key);
                    familiar.setNome(result.getString("nome"));
                    familiar.setParentesco(result.getString("parentesco"));
                    familiar.setData_nascimento(result.getDate("data_nascimento"));
                    familiar.setEstado_civil(result.getString("estado_civil"));
                    familiar.setOcupacao(result.getString("ocupacao"));
                    familiar.setEscolaridade(result.getString("escolaridade"));
                    familiar.setCandidatura(result.getInt("candidatura_id"));
                }
            } finally {
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return familiar;
    }

    @Override
    public Familiar put(Integer key, Familiar value) {
        String query;
        int autoGeneratedKeys;
        boolean isUpdate;
        if (key < 0) {
            isUpdate = false;
            query = INSERT_FAMILIAR;
            autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;
        }
        else {
            isUpdate = true;
            query = UPDATE_FAMILIAR;
            autoGeneratedKeys = Statement.NO_GENERATED_KEYS;
        }

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(query,autoGeneratedKeys);

            statement.setString(1,value.getNome());
            statement.setString(2,value.getParentesco());
            statement.setDate(3,value.getData_nascimento());
            statement.setString(4,value.getEstado_civil());
            statement.setString(5,value.getOcupacao());
            statement.setString(6,value.getEscolaridade());
            statement.setInt(7, value.getCandidatura());


            if (isUpdate) {
                statement.setInt(8, key);
            }

            statement.executeUpdate();

            try {
                if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
                    ResultSet keys = statement.getGeneratedKeys();
                    if (keys != null && keys.next()) {
                        value.setId(keys.getInt(1));
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
    public Familiar remove(Object key) {
        Familiar f = get(key);
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(DELETE_FAMILIAR);

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
        return f;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Familiar> m) {
        for(Familiar f : m.values())
            put(f.getId(), f);
    }

    @Override
    public void clear() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(DELETE_FAMILIARES);
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
    public Collection<Familiar> values() {
        ArrayList<Familiar> r = new ArrayList<>();
        try {
            Familiar familiar;

            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_FAMILIARES);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    familiar = new Familiar();
                    familiar.setId(result.getInt("id_pessoa"));
                    familiar.setNome(result.getString("nome"));
                    familiar.setParentesco(result.getString("parentesco"));
                    familiar.setData_nascimento(result.getDate("data_nascimento"));
                    familiar.setEstado_civil(result.getString("estado_civil"));
                    familiar.setOcupacao(result.getString("ocupacao"));
                    familiar.setEscolaridade(result.getString("escolaridade"));
                    familiar.setCandidatura(result.getInt("candidatura_id"));

                    r.add(familiar);
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

    @Override
    public Set<Entry<Integer, Familiar>> entrySet() {
        return null;
    }



    public List<Familiar> findByCandidatura(int id) {
        List<Familiar> familia = new ArrayList<>();
        try {

            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_CANDIDATURA);

            statement.setLong(1,id);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    if (containsKey(result.getInt("id_pessoa"))) {
                        Familiar familiar = get(result.getInt("id_pessoa"));
                        familia.add(familiar);
                    }
                }
            } finally {
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return familia;
    }

}
