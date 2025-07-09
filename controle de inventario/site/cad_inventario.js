const API_BASE_URL = 'http://localhost:8031/api/inventario/itens'; // URL base da API

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('cadastroInventarioForm');
    const mensagemStatus = document.getElementById('mensagemStatus');

    if (form) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault(); // Impede o envio padrão do formulário

            // Coleta os dados do formulário, incluindo os novos campos
            const itemData = {
                nome: document.getElementById('nomeItem').value,
                etiqueta: parseInt(document.getElementById('Etiqueta').value),
                numeroSerie: document.getElementById('numeroSerie').value,
                usuario: document.getElementById('usuario').value,
                observacoes: document.getElementById('observacoes').value,
                ativo: document.getElementById('ativo').checked, // Novo: valor do checkbox
                setor: document.getElementById('setor').value,   // Novo: valor do select
            };

            // Validação básica para a etiqueta ser um número válido
            if (isNaN(itemData.etiqueta)) {
                mensagemStatus.textContent = 'Por favor, insira um número válido para a etiqueta.';
                mensagemStatus.style.color = 'red';
                return;
            }

            // Validação para o campo setor
            if (!itemData.setor) {
                mensagemStatus.textContent = 'Por favor, selecione um setor para o item.';
                mensagemStatus.style.color = 'red';
                return;
            }

            mensagemStatus.textContent = 'Enviando dados...';
            mensagemStatus.style.color = 'black'; // Resetar a cor

            try {
                // Use a URL relativa para o proxy do Nginx
                const response = await fetch('/api/inventario/itens', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(itemData),
                });

                if (response.ok) {
                    mensagemStatus.textContent = 'Item cadastrado com sucesso!';
                    mensagemStatus.style.color = 'green';
                    form.reset(); // Limpa o formulário após o sucesso
                    // Garante que o checkbox 'ativo' volte ao estado padrão (checado)
                    document.getElementById('ativo').checked = true;
                    // Garante que o select 'setor' volte à opção padrão
                    document.getElementById('setor').value = '';
                } else {
                    const errorData = await response.json();
                    const errorMessage = errorData.message || response.statusText || 'Erro desconhecido';
                    mensagemStatus.textContent = `Erro ao cadastrar item: ${errorMessage}`;
                    mensagemStatus.style.color = 'red';

                    if (errorData.validationErrors) {
                        let validationMessages = Object.entries(errorData.validationErrors)
                                                        .map(([field, msg]) => `${field}: ${msg}`)
                                                        .join(', ');
                        mensagemStatus.textContent += ` (${validationMessages})`;
                    }
                }
            } catch (error) {
                console.error('Erro na requisição:', error);
                mensagemStatus.textContent = 'Erro ao conectar com o servidor.';
                mensagemStatus.style.color = 'red';
            }
        });
    } else {
        console.error("Erro: O formulário com ID 'cadastroInventarioForm' não foi encontrado no DOM.");
    }
});
