package Persistence;

import Model.Familiar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author davide on 06/12/14.
 */

@SuppressWarnings("UnusedDeclaration")
public class FamiliarRepository extends AbstractRepository<Familiar> {

    private static final String INSERT_FAMILIAR = "insert into familiar (nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade, candidatura_id) values (?,?,?,?,?,?,?)";
    private static final String UPDATE_FAMILIAR = "update familiar set nome = ?, parentesco = ?, data_nascimento = ?, estado_civil = ?, ocupacao = ?, escolaridade = ?, candidatura_id = ? where id = ?";

    private static final String SELECT_FAMILIAR = "select nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade from familiar where id = ?";
    private static final String SELECT_FAMILIARES = "select id, nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade from familiar";
    private static final String SELECT_BY_CANDIDATURA = "select id, nome, parentesco, data_nascimento, estado_civil, ocupacao, escolaridade from familiar where candidatura_id = ?";

    private static final String DELETE_FAMILIAR = "delete from familiar where id = ?";
    private static final String DELETE_FAMILIARES = "delete from familiar";

    private static final String COUNT_FAMILIARES = "select count(*) as n from familiar";

    private final String url;
    private final String user;
    private final String password;

    public FamiliarRepository(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public void save(Familiar entity) throws PersistenceException {
        String query;
        int autoGeneratedKeys;
        boolean isUpdate;
        if (entity.getId() < 0) {
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

            statement.setString(1,entity.getNome());
            statement.setString(2,entity.getParentesco());
            statement.setString(3,entity.getData_nascimento());
            statement.setString(4,entity.getEstado_civil());
            statement.setString(5,entity.getOcupacao());
            statement.setString(6,entity.getEscolaridade());
            statement.setLong(7,entity.getCandidatura_id());

            if (isUpdate) {
                statement.setLong(8,entity.getId());
            }

            statement.executeUpdate();

            try {
                if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
                    ResultSet keys = statement.getGeneratedKeys();
                    if (keys != null && keys.next()) {
                        entity.setId(keys.getLong(1));
                    }
                }
            } catch (SQLException ex) {
                throw new PersistenceException("Error generating id for family: " + entity);
            } finally {
                statement.close();
                connection.close();
            }

        } catch (SQLException ex) {
            throw new PersistenceException("Error saving family: " + entity, ex);
        }
    }

    @Override
    public Familiar find(long id) throws PersistenceException {
        try {
            Familiar familiar;

            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement statement = connection.prepareStatement(SELECT_FAMILIAR);

            statement.setLong(1,id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    familiar = new Familiar();
                    familiar.setId(id);
                    familiar.setNome(result.getString("nome"));
                    familiar.setParentesco(result.getString("parentesco"));
                    familiar.setData_nascimento(result.getString("data_nascimento"));
                    familiar.setEstado_civil(result.getString("estado_civil"));
                    familiar.setOcupacao(result.getString("ocupacao"));
                    familiar.setEscolaridade(result.getString("escolaridade"));
                }
                else {
                    familiar = null;
                }
            } finally {
                statement.close();
                connection.close();
            }

            return familiar;

        } catch (SQLException ex) {
            throw new PersistenceException("Error finding family members: " + id, ex);
        }
    }

    @Override
    public Iterable<Familiar> findAll() throws PersistenceException {
        return null;
    }

    @Override
    public void delete(Familiar entity) throws PersistenceException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(DELETE_FAMILIAR);

            statement.setLong(1,entity.getId());

            try {
                int rows = statement.executeUpdate();
                if (rows == 0) {
                    System.out.println("No family members removed");
                }
            } finally {
                statement.close();
                connection.close();
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Error deleting family members", ex);
        }
    }

    @Override
    public void deleteAll() throws PersistenceException {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(DELETE_FAMILIARES);
            } finally {
                connection.close();
            }
        }
        catch (SQLException ex) {
            throw new PersistenceException("Error deleting family members", ex);
        }
    }

    @Override
    public long count() throws PersistenceException {
        try {
            long count;

            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();

            try (ResultSet resultSet = statement.executeQuery(COUNT_FAMILIARES)) {
                if (resultSet.next())
                    count = resultSet.getLong("n");
                else
                    count = -1;
            } finally {
                statement.close();
                connection.close();
            }

            return count;

        } catch (SQLException ex) {
            throw new PersistenceException("Error counting family members", ex);
        }
    }

    public Iterable<Familiar> findByCandidatura(long id) throws PersistenceException{
        try {
            List<Familiar> familia = new ArrayList<>();

            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_CANDIDATURA);

            statement.setLong(1,id);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Familiar familiar = new Familiar();
                    familiar.setId(result.getLong("id"));

                    familia.add(familiar);
                }
            } finally {
                statement.close();
                connection.close();
            }

            return familia;


        } catch (SQLException ex) {
            throw new PersistenceException("Error finding family members of: " + id, ex);
        }

    }

}
