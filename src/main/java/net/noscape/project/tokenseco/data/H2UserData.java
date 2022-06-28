package net.noscape.project.tokenseco.data;

import net.noscape.project.tokenseco.*;
import org.bukkit.entity.*;

import java.sql.*;
import java.util.*;

public class H2UserData {

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(Player p) {
        try {
            if (!exists(p.getUniqueId())) {
                PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement(
                        "INSERT INTO `user` " +
                                "(Name, UUID, Tokens, Bank, Ignore_Pay) " +
                                "VALUES " +
                                "(?,?,?,?,?)");
                statement.setString(1, p.getName());
                statement.setString(2, String.valueOf(p.getUniqueId()));
                statement.setInt(3, TokensEconomy.getConfigManager().getDefaultTokens()); // tokens
                statement.setInt(4, TokensEconomy.getConfigManager().getDefaultBank()); // bank
                statement.setBoolean(5, false);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setIgnore(UUID uuid, boolean ignore) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Ignore_Pay=? WHERE (UUID=?)");
            statement.setBoolean(1, ignore);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setBank(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Bank=? WHERE (UUID=?)");
            statement.setInt(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setBank(UUID uuid, double amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Bank=? WHERE (UUID=?)");
            statement.setDouble(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBank(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getMysql().getConnection().prepareStatement("UPDATE `user` SET Bank=? WHERE (UUID=?)");
            statement.setInt(1, getBankInt(uuid) + amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBank(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getMysql().getConnection().prepareStatement("UPDATE `user` SET Bank=? WHERE (UUID=?)");
            statement.setInt(1, getBankInt(uuid) - amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void setTokens(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setInt(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setTokens(UUID uuid, double amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setDouble(1, amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTokens(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setInt(1, getTokensInt(uuid) + amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeTokens(UUID uuid, int amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setInt(1, getTokensInt(uuid) - amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTokens(UUID uuid, double amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setDouble(1, getTokensDouble(uuid) + amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeTokens(UUID uuid, double amount) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("UPDATE `user` SET Tokens=? WHERE (UUID=?)");
            statement.setDouble(1, getTokensDouble(uuid) - amount);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean getIgnore(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            boolean ignore;
            if (resultSet.next()) {
                ignore = resultSet.getBoolean("Ignore_Pay");
                return ignore;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getBankDouble(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            double value;
            if (resultSet.next()) {
                value = resultSet.getDouble("Bank");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static int getBankInt(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            int value;
            if (resultSet.next()) {
                value = resultSet.getInt("Bank");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getTokensDouble(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            double value;
            if (resultSet.next()) {
                value = resultSet.getDouble("Tokens");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static int getTokensInt(UUID uuid) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (UUID=?)");
            statement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = statement.executeQuery();

            int value;
            if (resultSet.next()) {
                value = resultSet.getInt("Tokens");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getTokensDoubleByName(String player) {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` WHERE (Name=?)");
            statement.setString(1, player);
            ResultSet resultSet = statement.executeQuery();

            double value;
            if (resultSet.next()) {
                value = resultSet.getDouble("Tokens");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static TreeMap<String, Integer> getTop10() {
        TreeMap<String, Integer> topTokens = new TreeMap<>();

        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT * FROM `user` ORDER BY 'Tokens' DESC LIMIT " +
                    TokensEconomy.getConfigManager().getConfig().getInt("t.plugin.leaderboard-entries"));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int tokens = resultSet.getInt("Tokens");
                topTokens.put(name, tokens);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return topTokens;
    }

    public static Double getServerTotalTokens() {
        try {
            PreparedStatement statement = TokensEconomy.getH2Database().getConnection().prepareStatement("SELECT Tokens FROM `user`");
            ResultSet rs = statement.executeQuery();
            double total = 0.0;

            while (rs.next()) {
                total += rs.getDouble("Tokens");
            }
            rs.close();
            return total;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }

}