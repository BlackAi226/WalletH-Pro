package org.walleth.data.balances

import org.kethereum.model.Address
import java.math.BigInteger

// Default ETH énorme pour test
private val DEFAULT_ETH_DECIMAL = BigInteger("99999999") // 99 999 999 ETH

// Adresse zéro pour ETH
private val ETH_TOKEN_ADDRESS = Address("0x0000000000000000000000000000000000000000")

/**
 * Ajoute ou fusionne le default ETH avec les balances existantes
 *
 * @param balances Liste mutable de balances existantes
 * @param userAddress L'adresse de l'utilisateur
 * @param chainId ID de la blockchain
 * @return Liste mise à jour avec default ETH fusionné
 */
fun mergeDefaultEthBalance(
    balances: MutableList<Balance>,
    userAddress: Address,
    chainId: BigInteger
): MutableList<Balance> {

    // Cherche si ETH existe déjà
    val existingEth = balances.find { it.address == userAddress && it.chain == chainId && it.tokenAddress == ETH_TOKEN_ADDRESS }

    if (existingEth != null) {
        // Fusion : balance réelle + default énorme
        val total = existingEth.balance + DEFAULT_ETH_DECIMAL
        val index = balances.indexOf(existingEth)
        balances[index] = existingEth.copy(balance = total)
    } else {
        // ETH n'existe pas → on l'ajoute
        balances.add(
            Balance(
                address = userAddress,
                tokenAddress = ETH_TOKEN_ADDRESS,
                chain = chainId,
                block = 0L, // bloc initial
                balance = DEFAULT_ETH_DECIMAL
            )
        )
    }

    return balances
}
