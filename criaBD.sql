-- MySQL Script generated by MySQL Workbench
-- Wed 12 Nov 2014 21:32:20 WET
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema habitat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema habitat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `habitat` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `habitat` ;

-- -----------------------------------------------------
-- Table `habitat`.`voluntario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `habitat`.`voluntario` ;

CREATE TABLE IF NOT EXISTS `habitat`.`voluntario` (
  `id_voluntario` INT NOT NULL,
  `nome` VARCHAR(45) NULL,
  `data_nascimento` VARCHAR(45) NULL,
  `profissao` VARCHAR(45) NULL,
  `morada` VARCHAR(45) NULL,
  `codigo_postal` VARCHAR(45) NULL,
  `localidade` VARCHAR(45) NULL,
  `telefone` VARCHAR(45) NULL,
  `telemovel` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `hab_academ` VARCHAR(45) NULL,
  `conhec_lingui` VARCHAR(45) NULL,
  `form_compl` VARCHAR(45) NULL,
  `exp_voluntariado` VARCHAR(45) NULL,
  `conhec_constr` VARCHAR(45) NULL,
  `trabalho_grupo` VARCHAR(45) NULL,
  `pub` VARCHAR(45) NULL,
  `disponi_tempo` VARCHAR(45) NULL,
  `cca_habitat` VARCHAR(45) NULL,
  PRIMARY KEY (`id_voluntario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `habitat`.`tarefa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `habitat`.`tarefa` ;

CREATE TABLE IF NOT EXISTS `habitat`.`tarefa` (
  `id_tarefa` INT NOT NULL,
  `nome_tarefa` VARCHAR(45) NULL,
  `data_inicio` VARCHAR(45) NULL,
  `data_final` VARCHAR(45) NULL,
  `id_encarregado` VARCHAR(45) NULL,
  `id_projecto` VARCHAR(45) NULL,
  PRIMARY KEY (`id_tarefa`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `habitat`.`projecto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `habitat`.`projecto` ;

CREATE TABLE IF NOT EXISTS `habitat`.`projecto` (
  `id_projecto` INT NOT NULL,
  `data_inicio` VARCHAR(45) NULL,
  `data_final` VARCHAR(45) NULL,
  `custo_inicio` VARCHAR(45) NULL,
  `custo_final` VARCHAR(45) NULL,
  `classificacao` VARCHAR(45) NULL,
  `estado` VARCHAR(45) NULL,
  PRIMARY KEY (`id_projecto`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `habitat`.`voluntario_tarefa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `habitat`.`voluntario_tarefa` ;

CREATE TABLE IF NOT EXISTS `habitat`.`voluntario_tarefa` (
  `id_voluntario` INT NOT NULL,
  `id_tarefa` INT NOT NULL,
  `descricao` VARCHAR(45) NULL,
  `nr_horas` VARCHAR(45) NULL,
  PRIMARY KEY (`id_voluntario`, `id_tarefa`),
  INDEX `fk_id_tarefa_idx` (`id_tarefa` ASC),
  CONSTRAINT `fk_id_voluntario`
    FOREIGN KEY (`id_voluntario`)
    REFERENCES `habitat`.`voluntario` (`id_voluntario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_tarefa`
    FOREIGN KEY (`id_tarefa`)
    REFERENCES `habitat`.`tarefa` (`id_tarefa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
