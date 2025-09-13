import kotlin.math.max
import kotlin.math.min

data class Pet(
    val nome: String,
    var fome: Int = 50,
    var felicidade: Int = 50,
    var cansaco: Int = 0,
    var idade: Int = 0,
    var vontadeXixi: Int = 0,
    var sujeira: Int = 0
) {
    // Alimentar: diminui fome, aumenta vontade de xixi
    fun alimentar(quantidade: Int) {
        println("Você alimentou $nome com $quantidade unidades de comida.")
        fome = min(100, fome - quantidade)
        if (fome < 0) fome = 0
// aumenta vontade de ir ao banheiro
        vontadeXixi = min(100, vontadeXixi + 20)
    }

    // Brincar: aumenta felicidade, mas aumenta cansaço, sujeira e vontade de xixi
    fun brincar(duracaoMinutos: Int) {
        println("Você brincou com $nome por $duracaoMinutos minutos.")
        val ganhoFelicidade = max(5, duracaoMinutos / 5) // ganho proporcional
        felicidade = min(100, felicidade + ganhoFelicidade)
// brincadeira aumenta cansaço e sujeira e vontade de xixi
        val aumentoCansaco = duracaoMinutos / 2
        cansaco = min(100, cansaco + aumentoCansaco)
        sujeira = min(100, sujeira + (duracaoMinutos / 3))
        vontadeXixi = min(100, vontadeXixi + 10)
    }

    // Descansar: o usuário informa quantas horas; 8h ou mais zera o cansaço
    fun descansar(horas: Int) {
        println("$nome está descansando por $horas horas...")
        if (horas >= 8) {
            cansaco = 0
            println("$nome descansou completamente.")
        } else {
            val reducao = horas * 10
            cansaco = max(0, cansaco - reducao)
            println("$nome recuperou $reducao pontos de cansaço.")
        }
    }

    // Verificar status (mostra todos os atributos)
    fun verificarStatus() {
        println("----- STATUS DE $nome -----")
        println("Idade: $idade")
        println("Fome: $fome / 100")
        println("Felicidade: $felicidade / 100")
        println("Cansaço: $cansaco / 100")
        println("Vontade de fazer xixi: $vontadeXixi / 100")
        println("Sujeira: $sujeira / 100")
        println("---------------------------")
    }

    // Ao passar tempo de 1 dia:
    fun passarTempo(dias: Int = 1) {
        repeat(dias) {
            idade += 1
            fome = min(100, fome + 5) // fome aumenta com o tempo
            felicidade = max(0, felicidade - 30) // queda grande por dia
            felicidade = max(0, felicidade - 3) // queda adicional de 3 por dia
            cansaco = min(100, cansaco + 10) // cansaço aumenta
// Observação: fome já tratada; sujeira e vontade não mudam só por passar o dia
        }
    }

    // Checa condições de derrota/vitória
    fun checarDerrota(): String? {
        if (fome >= 100) return "Derrota: $nome chegou a fome 100. ."
        if (cansaco >= 100) return "Derrota: $nome chegou a cansaço 100."
        if (felicidade <= 0) return "Derrota: $nome ficou sem felicidade."
        if (vontadeXixi >= 100) return "Derrota: $nome não aguentou segurar e isso causou um problema (vontade de xixi 100)."
        if (sujeira >= 100) return "Derrota: $nome estava muito sujo (sujeira 100)."
        return null
    }

    fun checarVitoria(): Boolean {
        return idade >= 50
    }
}

fun lerInt(prompt: String, default: Int? = null): Int {
    while (true) {
        print(prompt)
        val line = readLine()
        if (line.isNullOrBlank()) {
            if (default != null) return default
            println("Entrada inválida. Tente novamente.")
            continue
        }
        val n = line.toIntOrNull()
        if (n == null) {
            println("Por favor, digite um número válido.")
        } else return n
    }
}

fun main() {
    println("=== Voce acabou de adoptar o seu primer bichinho nenem! Parabens! ===")
    print("Digite o nome do seu bichinho: ")
    val nome = readLine().takeIf { !it.isNullOrBlank() } ?: "Bichinho"
    val pet = Pet(nome)

    mainLoop@ while (true) {
        println("\nEscolha uma opção:")
        println("1 - Alimentar o bichinho")
        println("2 - Brincar com o bichinho")
        println("3 - Descansar o bichinho")
        println("4 - Verificar status")
        println("5 - Sair do jogo")
        print("Opção: ")
        val opt = readLine()?.trim()

        when (opt) {
            "1" -> {
                val q = lerInt("Quantas unidades de comida deseja dar? (ex: 10) ", 10)
                pet.alimentar(q)
// sempre que alimenta, já atualizamos vontade de xixi no método alimentar
                pet.passarTempo(1)
            }
            "2" -> {
                val minutos = lerInt("Por quantos minutos vai brincar? (ex: 30) ", 30)
                pet.brincar(minutos)
                pet.passarTempo(1)
            }
            "3" -> {
                val horas = lerInt("Quantas horas de descanso? (1-24) ", 8)
                pet.descansar(horas)
                pet.passarTempo(1)
            }
            "4" -> {
                pet.verificarStatus()
// Verificar status também conta como um dia que passou (decidi assim para consistência)
                pet.passarTempo(1)
            }
            "5" -> {
                println("Saindo do jogo. Até a próxima!")
                break@mainLoop
            }
            else -> {
                println("Opção inválida. Tente novamente.")
                continue@mainLoop
            }
        }

// checa derrota ou vitória
        val derrotas = pet.checarDerrota()
        if (derrotas != null) {
            println("\n$derrotas")
            println("Fim de jogo. Você perdeu.")
            break@mainLoop
        }
        if (pet.checarVitoria()) {
            println("\nParabéns! $nome chegou à idade ${pet.idade} e você venceu o jogo! Voce é um bom cuidador")
            break@mainLoop
        }
    }
}