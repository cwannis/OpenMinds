-- OpenMinds - Formation Citoyenne
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS openminds /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE openminds;

CREATE TABLE IF NOT EXISTS user (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role enum('benevole','formateur','admin') NOT NULL DEFAULT 'benevole',
  ppLink text DEFAULT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uniq_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS formation (
  id int(11) NOT NULL AUTO_INCREMENT,
  titre varchar(255) NOT NULL,
  description text NOT NULL,
  thematique varchar(100) NOT NULL,
  type enum('online','in-person','both') NOT NULL DEFAULT 'online',
  imageUrl text DEFAULT NULL,
  content text DEFAULT NULL,
  videoUrl text DEFAULT NULL,
  duration_minutes int(11) DEFAULT 0,
  created_by int(11) DEFAULT NULL,
  datePubli datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  active tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  KEY FK_formation_creator (created_by),
  CONSTRAINT FK_formation_creator FOREIGN KEY (created_by) REFERENCES user (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS session (
  id int(11) NOT NULL AUTO_INCREMENT,
  formation_id int(11) NOT NULL,
  formateur_id int(11) DEFAULT NULL,
  date_debut datetime NOT NULL,
  date_fin datetime DEFAULT NULL,
  location varchar(255) DEFAULT NULL,
  max_participants int(11) DEFAULT 0,
  is_online tinyint(1) NOT NULL DEFAULT 0,
  meeting_link text DEFAULT NULL,
  active tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  KEY FK_session_formation (formation_id),
  KEY FK_session_formateur (formateur_id),
  CONSTRAINT FK_session_formation FOREIGN KEY (formation_id) REFERENCES formation (id) ON DELETE CASCADE,
  CONSTRAINT FK_session_formateur FOREIGN KEY (formateur_id) REFERENCES user (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS inscription (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  session_id int(11) NOT NULL,
  status enum('inscrit','present','absent','termine') NOT NULL DEFAULT 'inscrit',
  inscrit_le datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uniq_inscription (user_id, session_id),
  KEY FK_inscription_session (session_id),
  CONSTRAINT FK_inscription_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
  CONSTRAINT FK_inscription_session FOREIGN KEY (session_id) REFERENCES session (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS quiz (
  id int(11) NOT NULL AUTO_INCREMENT,
  formation_id int(11) NOT NULL,
  titre varchar(255) NOT NULL,
  passing_score int(11) NOT NULL DEFAULT 60,
  PRIMARY KEY (id),
  KEY FK_quiz_formation (formation_id),
  CONSTRAINT FK_quiz_formation FOREIGN KEY (formation_id) REFERENCES formation (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS quiz_question (
  id int(11) NOT NULL AUTO_INCREMENT,
  quiz_id int(11) NOT NULL,
  question text NOT NULL,
  option_a text NOT NULL,
  option_b text NOT NULL,
  option_c text NOT NULL,
  option_d text NOT NULL,
  correct_answer enum('a','b','c','d') NOT NULL,
  PRIMARY KEY (id),
  KEY FK_qq_quiz (quiz_id),
  CONSTRAINT FK_qq_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS quiz_result (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  quiz_id int(11) NOT NULL,
  score int(11) NOT NULL,
  total_questions int(11) NOT NULL,
  passed tinyint(1) NOT NULL DEFAULT 0,
  date_passage datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY FK_qr_user (user_id),
  KEY FK_qr_quiz (quiz_id),
  CONSTRAINT FK_qr_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
  CONSTRAINT FK_qr_quiz FOREIGN KEY (quiz_id) REFERENCES quiz (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS badge (
  id int(11) NOT NULL AUTO_INCREMENT,
  titre text NOT NULL,
  description text NOT NULL,
  imageUrl text NOT NULL,
  thematique varchar(100) DEFAULT NULL,
  datePubli timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS abadge (
  idBadge int(11) DEFAULT NULL,
  idUser int(11) DEFAULT NULL,
  dateObtention datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY FK__badge (idBadge),
  KEY FK__user (idUser),
  CONSTRAINT FK__badge FOREIGN KEY (idBadge) REFERENCES badge (id) ON DELETE CASCADE,
  CONSTRAINT FK__user FOREIGN KEY (idUser) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS formation_badge (
  formation_id int(11) NOT NULL,
  badge_id int(11) NOT NULL,
  PRIMARY KEY (formation_id, badge_id),
  KEY FK_fb_badge (badge_id),
  CONSTRAINT FK_fb_formation FOREIGN KEY (formation_id) REFERENCES formation (id) ON DELETE CASCADE,
  CONSTRAINT FK_fb_badge FOREIGN KEY (badge_id) REFERENCES badge (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE IF NOT EXISTS association (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description text NOT NULL,
  logoUrl text DEFAULT NULL,
  active tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

INSERT INTO user (id, name, email, password, role, ppLink) VALUES
(1, 'Admin OpenMinds', 'admin@openminds.local', '$2y$12$OkdYDLm4EXyNGQLjQ421puKVEsF9hPkIXt9OHy1IXzb1eAvTHJU7.', 'admin', NULL),
(2, 'Marie Formatrice', 'marie.formatrice@email.com', '$2y$12$BxsymjgekQkCLIs1.Ci0JuSA.EW3.xkwCerY4GARK2eoqM07MLgNq', 'formateur', NULL),
(3, 'Jean Benevole', 'jean.benevole@email.com', '$2y$12$BxsymjgekQkCLIs1.Ci0JuSA.EW3.xkwCerY4GARK2eoqM07MLgNq', 'benevole', NULL),
(4, 'Sophie Benevole', 'sophie.benevole@email.com', '$2y$12$BxsymjgekQkCLIs1.Ci0JuSA.EW3.xkwCerY4GARK2eoqM07MLgNq', 'benevole', NULL),
(5, 'Pierre Formateur', 'pierre.formateur@email.com', '$2y$12$BxsymjgekQkCLIs1.Ci0JuSA.EW3.xkwCerY4GARK2eoqM07MLgNq', 'formateur', NULL);

INSERT INTO association (id, name, description, logoUrl, active) VALUES
(1, 'France Benevolat', 'Reseau national de benevoles.', 'https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=200&h=200&fit=crop', 1),
(2, 'Croix-Rouge francaise', 'Mouvement humanitaire.', 'https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?w=200&h=200&fit=crop', 1),
(3, 'Restos du Coeur', 'Aide alimentaire.', 'https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?w=200&h=200&fit=crop', 1);

INSERT INTO formation (id, titre, description, thematique, type, imageUrl, content, duration_minutes, created_by, active) VALUES
(1, 'Comprendre l''inclusion sociale', 'Formation sur les enjeux de l''inclusion sociale et les actions concrètes.', 'Inclusion', 'both', 'https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=400&h=250&fit=crop', 'L''inclusion sociale est un processus qui vise à permettre à chaque individu de participer pleinement à la vie de la société. Elle concerne l''accès à l''emploi, au logement, à la santé, à l''éducation et à la culture. Les bénévoles jouent un rôle clé dans ce processus en accompagnant les personnes marginalisées et en favorisant le lien social.', 90, 2, 1),
(2, 'Egalité et non-discrimination', 'Principes d''égalité et moyens de combattre les discriminations.', 'Egalité', 'online', 'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=400&h=250&fit=crop', 'L''égalité est un principe fondamental qui garantit que chaque personne est traitée avec les mêmes droits et les mêmes opportunités. La discrimination peut être directe (refus explicite basé sur un critère prohibé) ou indirecte (règle apparemment neutre mais qui désavantage un groupe particulier).', 60, 2, 1),
(3, 'Ecologie et engagement citoyen', 'Agir concrètement pour l''environnement à l''échelle locale.', 'Environnement', 'both', 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400&h=250&fit=crop', 'L''engagement citoyen pour l''environnement passe par des actions concrètes : tri des déchets, réduction de la consommation d''énergie, mobilité douce, agriculture urbaine, sensibilisation du voisinage. Chaque geste compte pour préserver notre planète.', 120, 5, 1),
(4, 'Tolérance et diversité culturelle', 'Reconnaître et valoriser la diversité culturelle.', 'Tolérance', 'online', 'https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=400&h=250&fit=crop', 'La diversité culturelle est une richesse qui doit être protégée et valorisée. La tolérance ne signifie pas l''indifférence mais la reconnaissance active de la valeur de chaque culture. Les préjugés sont des obstacles à surmonter par l''éducation et le dialogue.', 75, 2, 1),
(5, 'Engagement citoyen et benevolat', 'Comprendre et vivre son engagement citoyen.', 'Engagement', 'in-person', 'https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=400&h=250&fit=crop', 'L''engagement citoyen est un acte volontaire par lequel une personne décide de contribuer au bien commun. Le bénévolat est la forme la plus répandue de cet engagement. Il existe de nombreuses formes d''engagement : ponctuel, régulier, en ligne, en présentiel.', 45, 5, 1),
(6, 'Respect et droits humains', 'Comprendre la Déclaration Universelle des Droits de l''Homme.', 'Respect', 'online', 'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=400&h=250&fit=crop', 'Les droits humains sont universels, inaliénables et indivisibles. Ils protègent la dignité de chaque personne. Cette formation explore les articles fondamentaux de la Déclaration Universelle de 1948 et leur application dans la vie quotidienne.', 60, 2, 1);

INSERT INTO session (id, formation_id, formateur_id, date_debut, date_fin, location, max_participants, is_online, meeting_link, active) VALUES
(1, 1, 2, '2026-04-15 09:00:00', '2026-04-15 12:00:00', 'Maison des Associations - Paris 10e', 20, 0, NULL, 1),
(2, 1, 2, '2026-04-20 14:00:00', '2026-04-20 17:00:00', NULL, 30, 1, 'https://meet.jit.si/OpenMinds-Inclusion', 1),
(3, 2, 2, '2026-04-10 10:00:00', '2026-04-10 11:00:00', NULL, 50, 1, 'https://meet.jit.si/OpenMinds-Egalite', 1),
(4, 3, 5, '2026-04-18 09:00:00', '2026-04-18 12:00:00', 'Parc de la Villette - Paris 19e', 25, 0, NULL, 1),
(5, 4, 2, '2026-04-22 14:00:00', '2026-04-22 15:15:00', NULL, 40, 1, 'https://meet.jit.si/OpenMinds-Tolerance', 1),
(6, 5, 5, '2026-04-25 10:00:00', '2026-04-25 11:00:00', 'Maison des Associations - Paris 10e', 15, 0, NULL, 1),
(7, 6, 2, '2026-04-28 16:00:00', '2026-04-28 17:00:00', NULL, 50, 1, 'https://meet.jit.si/OpenMinds-DroitsHumains', 1);

INSERT INTO inscription (id, user_id, session_id, status, inscrit_le) VALUES
(1, 3, 1, 'inscrit', '2026-03-20 10:00:00'),
(2, 4, 1, 'inscrit', '2026-03-21 14:00:00'),
(3, 3, 3, 'termine', '2026-03-15 09:00:00'),
(4, 4, 3, 'present', '2026-03-15 09:30:00'),
(5, 3, 5, 'inscrit', '2026-03-25 11:00:00');

INSERT INTO quiz (id, formation_id, titre, passing_score) VALUES
(1, 1, 'Quiz - Inclusion sociale', 60),
(2, 2, 'Quiz - Egalite et non-discrimination', 60),
(3, 3, 'Quiz - Ecologie et engagement', 60),
(4, 4, 'Quiz - Tolerance et diversite', 60),
(5, 6, 'Quiz - Droits humains', 60);

INSERT INTO quiz_question (id, quiz_id, question, option_a, option_b, option_c, option_d, correct_answer) VALUES
(1, 1, 'Qu''est-ce que l''inclusion sociale ?', 'L''isolement des minorites', 'Le processus permettant a chacun de participer pleinement a la societe', 'Un programme gouvernemental obligatoire', 'Une forme de charity', 'b'),
(2, 1, 'Quel est un obstacle a l''inclusion ?', 'L''acces a l''education', 'La discrimination a l''embauche', 'Le benevolat', 'La diversite culturelle', 'b'),
(3, 1, 'Comment un benevole peut-il favoriser l''inclusion ?', 'En ignorant les differences', 'En accompagnant les personnes marginalisees', 'En refusant l''aide aux etrangers', 'En restant dans sa zone de confort', 'b'),
(4, 2, 'Qu''est-ce que la discrimination indirecte ?', 'Un refus explicite', 'Une regle apparemment neutre qui desavantage un groupe', 'Une blague offensive', 'Un compliment mal place', 'b'),
(5, 2, 'Quel texte francais interdit les discriminations ?', 'Le Code de la route', 'Le Code penal et la loi de 1972', 'Le Code des impots', 'Le Code du sport', 'b'),
(6, 2, 'Combien de criteres de discrimination sont reconnus par la loi ?', '5', '10', '25', 'Plus de 25', 'd'),
(7, 3, 'Qu''est-ce que l''agriculture urbaine ?', 'Cultiver en milieu rural', 'Produire des aliments en ville', 'Elever des animaux dans les forets', 'Importer des fruits exotiques', 'b'),
(8, 3, 'Quel est un geste ecologique au quotidien ?', 'Jeter ses dechets dans la nature', 'Utiliser les transports en commun', 'Laisser la lumiere allumee', 'Acheter du jetable', 'b'),
(9, 4, 'La tolerance signifie :', 'L''indifference', 'La reconnaissance active de la valeur de chaque culture', 'L''acceptation de l''injustice', 'Le rejet des differences', 'b'),
(10, 4, 'Qu''est-ce qu''un prejuge ?', 'Une opinion fondee sur des faits', 'Une idee preconcue non verifiee', 'Une loi universelle', 'Un droit fondamental', 'b'),
(11, 5, 'Quand a ete adoptee la Declaration Universelle des Droits de l''Homme ?', '1789', '1945', '1948', '1989', 'c'),
(12, 5, 'Les droits humains sont :', 'Reserves aux citoyens', 'Universels et inalienables', 'Negociables', 'Differents selon les pays', 'b');

INSERT INTO quiz_result (id, user_id, quiz_id, score, total_questions, passed, date_passage) VALUES
(1, 3, 1, 3, 3, 1, '2026-04-16 12:30:00'),
(2, 3, 2, 2, 3, 1, '2026-04-11 11:30:00'),
(3, 4, 2, 3, 3, 1, '2026-04-11 11:45:00'),
(4, 3, 4, 2, 2, 1, '2026-04-23 15:00:00');

INSERT INTO badge (id, titre, description, imageUrl, thematique) VALUES
(1, 'Inclusion', 'Formation Inclusion Sociale validee', 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=100&h=100&fit=crop', 'Inclusion'),
(2, 'Egalite', 'Formation Egalite validee', 'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=100&h=100&fit=crop', 'Egalite'),
(3, 'Ecologie', 'Formation Ecologie validee', 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=100&h=100&fit=crop', 'Environnement'),
(4, 'Tolerance', 'Formation Tolerance validee', 'https://images.unsplash.com/photo-1517486808906-6ca8b3f04846?w=100&h=100&fit=crop', 'Tolerance'),
(5, 'Engagement', 'Formation Engagement validee', 'https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=100&h=100&fit=crop', 'Engagement'),
(6, 'Droits Humains', 'Formation Droits Humains validee', 'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=100&h=100&fit=crop', 'Respect');

INSERT INTO formation_badge (formation_id, badge_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6);

INSERT INTO abadge (idBadge, idUser, dateObtention) VALUES
(1, 3, '2026-04-16 12:30:00'),
(2, 3, '2026-04-11 11:30:00'),
(4, 3, '2026-04-23 15:00:00'),
(2, 4, '2026-04-11 11:45:00');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
