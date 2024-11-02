package com.pavelb.hometask.domain.encryption

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher

// This is a questionable decision - we should not have a dependency on Android SDK in the domain layer,
// but the presentation layer is also not a good place for encryption logic. As usual with cases where
// android context is required for business logic.
interface BiometricDelegate {
    fun getCipher(): Cipher
    fun getDecryptCipher(iv: ByteArray): Cipher
    fun authenticate(context: FragmentActivity, cipher: Cipher, onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit)
}