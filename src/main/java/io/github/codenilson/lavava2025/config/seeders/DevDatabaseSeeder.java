package io.github.codenilson.lavava2025.config.seeders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerPerformanceService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevDatabaseSeeder implements CommandLineRunner {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final MatchService matchService;
    private final PlayerPerformanceService playerPerformanceService;
    private final ValorantMapRepository valorantMapRepository;

    // Dados realistas do Valorant
    private static final List<String> VALORANT_MAPS = Arrays.asList(
            "Ascent", "Bind", "Haven", "Split", "Icebox", "Breeze", "Fracture", "Pearl", "Lotus", "Sunset");

    private static final List<String> VALORANT_AGENTS = Arrays.asList(
            // Duelistas
            "Jett", "Phoenix", "Reyna", "Raze", "Yoru", "Neon", "Iso",
            // Iniciadores
            "Sova", "Breach", "Skye", "KAY/O", "Fade", "Gekko",
            // Controladores
            "Brimstone", "Omen", "Viper", "Astra", "Harbor", "Clove",
            // Sentinelas
            "Sage", "Cypher", "Killjoy", "Chamber", "Deadlock", "Vyse");

    private static final List<String> PLAYER_NAMES = Arrays.asList(
            "TenZ", "ScreaM", "s1mple", "ShahZaM", "SicK", "dapr", "zombs", "crashies", "Victor", "yay",
            "Aspas", "Less", "Sacy", "pANcada", "Saadhak", "dgzin", "cauanzin", "tuyz", "qck", "Loud_cortezia");

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🎮 Iniciando configuração do ambiente de desenvolvimento...");

        // 1. Criar todos os mapas do Valorant
        List<ValorantMap> maps = createValorantMaps();
        System.out.println("✅ " + maps.size() + " mapas do Valorant criados");

        // 2. Criar jogadores com nomes realistas
        List<Player> players = createPlayers();
        System.out.println("✅ " + players.size() + " jogadores criados");

        // 3. Atribuir roles aos jogadores
        assignPlayerRoles(players);
        System.out.println("✅ Roles atribuídas aos jogadores");

        // 4. Criar partidas diversificadas com performances detalhadas
        createDetailedMatches(players, maps);
        System.out.println("✅ Partidas com performances detalhadas criadas");

        System.out.println("🚀 Ambiente de desenvolvimento configurado com sucesso!");
        System.out.println("📊 Sistema de ranking populado com dados realistas!");
        System.out.println("🎯 Total de jogadores: " + players.size());
        System.out.println("🗺️ Total de mapas: " + maps.size());
    }

    /**
     * Cria todos os mapas oficiais do Valorant
     */
    private List<ValorantMap> createValorantMaps() {
        List<ValorantMap> maps = new ArrayList<>();

        for (String mapName : VALORANT_MAPS) {
            ValorantMap map = valorantMapRepository.findByName(mapName).orElseGet(() -> {
                ValorantMap newMap = new ValorantMap();
                newMap.setName(mapName);
                // URLs de imagens dos mapas (podem ser adicionadas posteriormente)
                newMap.setSplashUrl("https://valorant-assets.com/maps/" + mapName.toLowerCase() + "/splash.jpg");
                return valorantMapRepository.save(newMap);
            });
            maps.add(map);
        }

        return maps;
    }

    /**
     * Cria jogadores com nomes realistas do cenário competitivo
     */
    private List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < PLAYER_NAMES.size(); i++) {
            Player player = new Player();
            player.setUsername(PLAYER_NAMES.get(i));
            player.setPassword("Abc@123456");

            // Simular alguns Discord IDs para realismo
            if (random.nextBoolean()) {
                player.setDiscordId(1000000000L + random.nextInt(999999999));
            }

            playerService.save(player);
            players.add(player);
        }

        return players;
    }

    /**
     * Atribui roles aos jogadores
     */
    private void assignPlayerRoles(List<Player> players) {
        // Primeiro jogador é ADMIN + PLAYER
        if (!players.isEmpty()) {
            playerService.addRoles(players.get(0).getId(), Set.of(Roles.ADMIN, Roles.PLAYER));
        }

        // Segundo jogador também pode ser ADMIN para testes
        if (players.size() > 1) {
            playerService.addRoles(players.get(1).getId(), Set.of(Roles.ADMIN, Roles.PLAYER));
        }

        // Resto dos jogadores são apenas PLAYER
        for (int i = 2; i < players.size(); i++) {
            playerService.addRoles(players.get(i).getId(), Set.of(Roles.PLAYER));
        }
    }

    /**
     * Cria partidas detalhadas com performances realistas
     */
    private void createDetailedMatches(List<Player> players, List<ValorantMap> maps) {
        // Criar 8 partidas variadas para ter dados suficientes para ranking
        for (int matchIndex = 0; matchIndex < 8; matchIndex++) {
            ValorantMap selectedMap = maps.get(random.nextInt(maps.size()));

            System.out.println("🎮 Criando partida " + (matchIndex + 1) + "/8 no mapa: " + selectedMap.getName());

            // Criar partida
            Match match = new Match(selectedMap);
            matchService.save(match);

            // Dividir jogadores em 2 times (2-5 jogadores por time, sem repetição)
            List<Player> shuffledPlayers = new ArrayList<>(players);
            java.util.Collections.shuffle(shuffledPlayers);

            int team1Size = Math.min(2 + random.nextInt(4), players.size() / 2); // 2-5 jogadores, máximo metade dos
                                                                                 // jogadores
            int maxTeam2Size = Math.min(5, players.size() - team1Size);
            int team2Size = Math.max(2, Math.min(2 + random.nextInt(4), maxTeam2Size));

            // Garantir que não há sobreposição entre times
            List<Player> team1Players = shuffledPlayers.subList(0, team1Size);
            List<Player> team2Players = shuffledPlayers.subList(team1Size, team1Size + team2Size);

            Team team1 = createTeamWithPlayers(match, team1Players);
            Team team2 = createTeamWithPlayers(match, team2Players);

            // Determinar vencedor aleatoriamente
            boolean team1Wins = random.nextBoolean();
            Team winner = team1Wins ? team1 : team2;
            Team loser = team1Wins ? team2 : team1;

            match.setWinner(winner);
            match.setLoser(loser);
            matchService.save(match);

            System.out.println("  📊 Time 1: " + team1Players.size() + " jogadores");
            System.out.println("  📊 Time 2: " + team2Players.size() + " jogadores");
            System.out.println("  🏆 Vencedor: Time " + (team1Wins ? "1" : "2"));

            // Criar performances detalhadas para cada jogador
            createPlayerPerformances(team1, match, team1Wins);
            createPlayerPerformances(team2, match, !team1Wins);
        }
    }

    /**
     * Cria um time com jogadores específicos
     */
    private Team createTeamWithPlayers(Match match, List<Player> teamPlayers) {
        Team team = new Team();
        team.setMatch(match);

        for (Player player : teamPlayers) {
            team.getPlayers().add(player);
        }

        return teamService.createTeam(team);
    }

    /**
     * Cria performances realistas para jogadores de um time
     */
    private void createPlayerPerformances(Team team, Match match, boolean isWinnerTeam) {
        for (Player player : team.getPlayers()) {
            // Verificar se já existe performance para este jogador nesta partida
            try {
                playerPerformanceService.findByPlayerAndMatch(player.getId(), match.getId());
                System.out.println("⚠️  Performance já existe para jogador " + player.getUsername() + " na partida "
                        + match.getId());
                continue; // Pular este jogador se já tem performance
            } catch (jakarta.persistence.EntityNotFoundException e) {
                // Performance não existe, criar nova
            }

            PlayerPerformance performance = new PlayerPerformance(player, team, match);

            // Estatísticas baseadas em dados reais do Valorant
            if (isWinnerTeam) {
                // Time vencedor tem estatísticas melhores
                performance.setKills(15 + random.nextInt(15)); // 15-29 kills
                performance.setDeaths(8 + random.nextInt(10)); // 8-17 deaths
                performance.setAssists(3 + random.nextInt(8)); // 3-10 assists
            } else {
                // Time perdedor tem estatísticas um pouco piores
                performance.setKills(8 + random.nextInt(15)); // 8-22 kills
                performance.setDeaths(10 + random.nextInt(12)); // 10-21 deaths
                performance.setAssists(2 + random.nextInt(7)); // 2-8 assists
            }

            // Chance pequena de ace (1 em 20)
            if (random.nextInt(20) == 0) {
                performance.setAce(1);
            }

            // Agente aleatório
            String selectedAgent = VALORANT_AGENTS.get(random.nextInt(VALORANT_AGENTS.size()));
            performance.setAgent(selectedAgent);

            try {
                playerPerformanceService.save(performance);
                System.out.println("✅ Performance criada para " + player.getUsername() + " com " + selectedAgent);
            } catch (Exception e) {
                System.out.println("❌ Erro ao criar performance para " + player.getUsername() + ": " + e.getMessage());
            }
        }
    }
}